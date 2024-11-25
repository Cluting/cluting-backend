package com.cluting.clutingbackend.docEval.service;

import com.cluting.clutingbackend.docEval.dto.*;
import com.cluting.clutingbackend.docEval.repository.*;
import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.plan.repository.PartRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public EvaluationResponse getEvaluationDetails(Long applicationId, Long clubId, Long postId, String partName, Long clubUserId) {
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

        // 2. Part ID 가져오기
        Part part = partRepository.findByNameAndPostId(partName, postId)
                .orElseThrow(() -> new IllegalArgumentException("Part not found with name: " + partName));

        // 3. 내 평가 가져오기 (여러 Criteria를 처리)
        List<Criteria> myCriteria = criteriaRepository.findByApplication_ApplicationIdAndClubUser_ClubUserIdAndPart_PartId(applicationId, clubUserId, part.getPartId());
        List<CriteriaScore> myCriteriaScores = myCriteria.stream()
                .map(criteria -> new CriteriaScore(criteria.getName(), criteria.getScore()))
                .distinct() // 중복 제거
                .toList();

        Integer myTotalScore = myCriteriaScores.stream()
                .mapToInt(CriteriaScore::getScore)
                .sum();

        List<Evaluation> myEvaluations = evaluationRepository.findByApplication_ApplicationIdAndClubUser_ClubUserId(applicationId, clubUserId); // 수정
        String myComment = myEvaluations.isEmpty() ? null : myEvaluations.get(0).getComment(); // 여러 평가가 있을 경우 첫 번째 평가의 댓글 가져오기

        List<ScoreDetail> scoreDetails = myCriteriaScores.stream()
                .map(criteriaScore -> new ScoreDetail(criteriaScore.getName(), criteriaScore.getScore()))
                .collect(Collectors.toList());

        MyEvaluation myEvaluation = MyEvaluation.builder()
                .scoreDetails(scoreDetails)
                .comment(myComment)
                .build();

        // 4. 다른 운영진 평가 가져오기 (여러 운영진 평가 처리)
        List<Evaluation> otherEvaluations = evaluationRepository.findByApplication_ApplicationId(applicationId);
        Map<Long, AdminEvaluation> adminEvaluations = new HashMap<>();

        for (Evaluation evaluation : otherEvaluations) {
            if (evaluation.getClubUser().getClubUserId().equals(clubUserId)) continue; // 본인은 제외

            Long evaluatorId = evaluation.getClubUser().getClubUserId();
            User evaluator = evaluation.getClubUser().getUser();

            // AdminEvaluation에 해당 평가자 정보 저장
            AdminEvaluation adminEvaluation = adminEvaluations.computeIfAbsent(evaluatorId, id -> AdminEvaluation.builder()
                    .name(evaluator.getName())
                    .totalScore(0)
                    .criteriaScores(new ArrayList<>())
                    .comment(evaluation.getComment())
                    .build());

            // 여러 Criteria가 있을 수 있으므로 getResultList() 사용
            List<Criteria> criteriaList = criteriaRepository.findByApplication_ApplicationIdAndClubUser_ClubUserIdAndPart_PartId(applicationId, evaluatorId, part.getPartId());
            List<CriteriaScore> criteriaScores = criteriaList.stream()
                    .map(criteria -> new CriteriaScore(criteria.getName(), criteria.getScore()))
                    .distinct()
                    .toList();

            // 평가 점수 합산
            int totalScore = criteriaScores.stream().mapToInt(CriteriaScore::getScore).sum();
            adminEvaluation.setTotalScore(adminEvaluation.getTotalScore() + totalScore);
            adminEvaluation.getCriteriaScores().addAll(criteriaScores);
        }

        // 5. 인재상 가져오기
        List<Part> parts = partRepository.findByPostId(postId);
        List<Map<String, Object>> talentProfiles = parts.stream()
                .map(partObj -> {
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("partName", partObj.getName());
                    List<String> descriptions = talentProfileRepository.findByPartId(partObj.getPartId()).stream()
                            .map(TalentProfile::getDescription)
                            .toList();
                    profile.put("descriptions", descriptions);
                    return profile;
                })
                .toList();

        return EvaluationResponse.builder()
                .applicantInfo(applicantInfo)
                .questionContents(getQuestionContents(applicationId)) // 지원서 문항과 답변
                .myEvaluations(myEvaluation)
                .adminEvaluations(new ArrayList<>(adminEvaluations.values()))
                .talentProfiles(talentProfiles)
                .build();
    }


    // 지원서 문항과 답변 가져오기 (helper method)
    private List<QuestionContent> getQuestionContents(Long applicationId) {
        List<Answer> answers = answerRepository.findByApplication_ApplicationId(applicationId);
        return answers.stream()
                .map(answer -> new QuestionContent(answer.getQuestionId(), answer.getContent()))
                .toList();
    }


    @Transactional
    public void evaluateDocument(Long applicationId, Long clubId, Long postId, String partName, Long clubUserId, EvaluationRequest evaluationRequest) {

        // 1. 지원서 및 관련 객체 조회
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + applicationId));

        ClubUser clubUser = clubUserRepository.findById(clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("ClubUser not found with id: " + clubUserId));

        // partName과 applicationId로 Part 조회
        Part part = partRepository.findByNameAndPostId(partName, postId)
                .orElseThrow(() -> new IllegalArgumentException("Part not found with name: " + partName + " and postId: " + postId));

        // 2. 기준 이름별 점수 저장
        Set<String> uniqueNames = new HashSet<>();
        int totalScore = 0; // 총합 계산

        for (CriteriaScoreDto scoreDto : evaluationRequest.getCriteriaScores()) {
            String criteriaName = scoreDto.getName();

            // 중복 제거
            if (!uniqueNames.add(criteriaName)) {
                continue;
            }

            // Criteria 조회 또는 생성
            Criteria criteria = criteriaRepository.findByApplicationAndPartAndName(application, part, criteriaName)
                    .orElseGet(() -> {
                        Criteria newCriteria = new Criteria();
                        newCriteria.setApplication(application);
                        newCriteria.setPart(part);
                        newCriteria.setName(criteriaName);
                        return newCriteria;
                    });

            // 점수 및 클럽 운영진 저장
            criteria.setScore(scoreDto.getScore());
            criteria.setClubUser(clubUser);
            criteriaRepository.save(criteria);

            // 총합 계산
            totalScore += scoreDto.getScore();
        }

        // 3. Evaluation 저장
        Evaluation evaluation = evaluationRepository.findByApplicationAndClubUserAndStage(application, clubUser, Evaluation.Stage.DOCUMENT)
                .orElseGet(() -> {
                    Evaluation newEvaluation = new Evaluation();
                    newEvaluation.setApplication(application);
                    newEvaluation.setClubUser(clubUser);
                    newEvaluation.setStage(Evaluation.Stage.DOCUMENT);
                    return newEvaluation;
                });

        evaluation.setScore(totalScore); // 총점 저장
        evaluation.setComment(evaluationRequest.getComment()); // 코멘트 저장
        evaluationRepository.save(evaluation);

        // 4. Application 점수 업데이트 로직
        Integer currentNumClubUser = application.getNumClubUser();
        Integer currentScore = application.getScore();

        if (currentNumClubUser == null || currentScore == null) {
            // 초기값 설정
            application.setNumClubUser(1);
            application.setScore(totalScore);
        } else {
            // 평균 계산 후 업데이트
            int newNumClubUser = currentNumClubUser + 1;
            int newScore = (currentScore * currentNumClubUser + totalScore) / newNumClubUser;
            application.setNumClubUser(newNumClubUser);
            application.setScore(newScore);
        }

        applicationRepository.save(application);
    }



}
