package com.cluting.clutingbackend.docEval.service;

import com.cluting.clutingbackend.docEval.dto.*;
import com.cluting.clutingbackend.docEval.repository.*;
import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.plan.repository.PartRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Evaluation2Service {

    private final Application2Repository applicationRepository;
    private final Answer2Repository answerRepository;
    private final Evaluation2Repository evaluationRepository;
    private final User2Repository userRepository;
    private final TalentProfile2Repository talentProfileRepository;
    private final Criteria2Repository criteriaRepository;
    private final PartRepository partRepository;
    private final ClubUser2Repository clubUserRepository;

    public EvaluationResponse getEvaluationDetails(Long applicationId, Long clubId, Long postId, Long clubUserId) {

        // 1. 지원자 정보 가져오기
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));
        User applicant = application.getUser();

        ApplicantInfo applicantInfo = ApplicantInfo.builder()
                .name(applicant.getName())
                .phone(applicant.getPhone())
                .location(applicant.getLocation())
                .school(applicant.getSchool())
                .major(applicant.getMajor())
                .doubleMajor(applicant.getDoubleMajor())
                .semester(applicant.getSemester().name())
                .profile(applicant.getProfile())
                .build();

        // 2. 지원서 내용 가져오기
        List<Answer> answers = answerRepository.findByApplication_ApplicationId(applicationId);
        List<QuestionContent> questionContents = answers.stream()
                .map(answer -> new QuestionContent(answer.getQuestionId(), answer.getContent()))
                .toList();

        // 3. 내 평가 가져오기
        List<Evaluation> myEvaluations = evaluationRepository.findByClubUser_UserIdAndApplication_ApplicationId(clubUserId, applicationId);
        List<ScoreDetail> scoreDetails = myEvaluations.stream()
                .map(evaluation -> new ScoreDetail(evaluation.getCriteria().getCriteriaId(), evaluation.getScore()))
                .toList();

        String comment = myEvaluations.isEmpty() ? null : myEvaluations.get(0).getComment();

        // 4. 인재상 가져오기
        List<Part> parts = partRepository.findByPostId(postId); // 해당 모집공고에 속한 파트 조회
        List<Map<String, Object>> talentProfiles = new ArrayList<>();

        for (Part part : parts) {
            Map<String, Object> partProfile = new HashMap<>();
            partProfile.put("partName", part.getName()); // 파트 이름 저장

            // 해당 Part에 속하는 TalentProfile 가져오기
            List<String> descriptions = new ArrayList<>();
            List<TalentProfile> profiles = talentProfileRepository.findByPartId(part.getPartId());
            for (TalentProfile profile : profiles) {
                descriptions.add(profile.getDescription()); // 인재상 설명 저장
            }

            partProfile.put("descriptions", descriptions); // 인재상 설명들을 descriptions 키로 저장
            talentProfiles.add(partProfile); // 결과 리스트에 추가
        }


        // 5. 다른 운영진 평가 가져오기
        List<Evaluation> allEvaluations = evaluationRepository.findByApplication_ApplicationId(applicationId);
        Map<Long, AdminEvaluation> adminEvaluations = new HashMap<>();

        for (Evaluation evaluation : allEvaluations) {
            Long evaluatorId = evaluation.getClubUser().getClubUserId();
            User evaluator = evaluation.getClubUser().getUser();

            AdminEvaluation adminEvaluation = adminEvaluations.computeIfAbsent(evaluatorId, id -> AdminEvaluation.builder()
                    .name(evaluator.getName())
                    .totalScore(evaluation.getScore())
                    .criteriaScores(new ArrayList<>())
                    .comment(evaluation.getComment())
                    .build());

            Criteria criteria = evaluation.getCriteria();
            adminEvaluation.getCriteriaScores().add(new CriteriaScore(criteria.getName(), evaluation.getScore()));
        }

        return EvaluationResponse.builder()
                .applicantInfo(applicantInfo)
                .questionContents(questionContents)
                .myEvaluations(new MyEvaluation(scoreDetails, comment))
                .talentProfiles(talentProfiles) // 인재상 정보 포함
                .adminEvaluations(new ArrayList<>(adminEvaluations.values()))
                .build();
    }

    @Transactional
    public void evaluateDocument(Long applicationId, Long clubId, Long postId, Long clubUserId, EvaluationRequest evaluationRequest) {

        // 1. 기준 이름별 점수 저장
        for (CriteriaScoreDto scoreDto : evaluationRequest.getCriteriaScores()) {
            // 기준 이름을 통해 Criteria 엔티티를 찾음
            Criteria criteria = criteriaRepository.findByName(scoreDto.getName())
                    .orElseThrow(() -> new IllegalArgumentException("Criteria not found for name: " + scoreDto.getName()));

            // 점수 저장
            criteria.setScore(scoreDto.getScore());
            criteriaRepository.save(criteria);

            // 평가 객체 생성 및 저장
            Application application = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

            ClubUser clubUser = clubUserRepository.findById(clubUserId)
                    .orElseThrow(() -> new IllegalArgumentException("ClubUser not found with id: " + clubUserId));

            Evaluation evaluation = new Evaluation();
            evaluation.setCriteria(criteria);
            evaluation.setApplication(application);
            evaluation.setClubUser(clubUser);
            evaluation.setStage(Evaluation.Stage.DOCUMENT);
            evaluation.setScore(scoreDto.getScore());

            evaluationRepository.save(evaluation);
        }
// 2. 코멘트 저장
        String comment = evaluationRequest.getComment();
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

        // 기존 평가 정보를 찾을 때 첫 번째 평가를 찾고 없으면 예외를 던짐
        List<Evaluation> evaluations = evaluationRepository.findByApplicationAndClubUserAndStage(application,
                clubUserRepository.findById(clubUserId).get(), Evaluation.Stage.DOCUMENT);

        if (evaluations.isEmpty()) {
            throw new IllegalArgumentException("Evaluation not found for application and clubUser");
        }

        // 첫 번째 평가 객체 가져오기 (필요한 경우 여러 평가가 있을 수 있으므로, 조건에 맞는 평가를 찾아야 함)
        Evaluation evaluationForComment = evaluations.get(0);

        evaluationForComment.setComment(comment);
        evaluationRepository.save(evaluationForComment);
    }
}
