package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.*;
import com.cluting.clutingbackend.global.enums.CurrentStage;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.domain.DocumentCriteria;
import com.cluting.clutingbackend.plan.domain.DocumentEvalScore;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentCriteriaRepository;
import com.cluting.clutingbackend.plan.repository.DocumentEvalScoreRepository;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final RecruitRepository recruitRepository;
    private final GroupRepository groupRepository;
    private final DocumentEvalScoreRepository documentEvalScoreRepository;
    private final DocumentCriteriaRepository documentCriteriaRepository;

    // 모집 공고 확인
    private void ensureRecruitExists(Long recruitId) {
        if (!recruitRepository.existsById(recruitId)) {
            throw new IllegalArgumentException("모집 공고가 존재하지 않습니다.");
        }
    }

    // 공통 필터 및 정렬 처리
    private List<DocumentEvaluationResponse> filterAndSort(List<Application> applications, DocumentEvaluationRequest request, String stage, CustomUserDetails currentUser, Long recruitId) {
        Long currentClubUserId = currentUser.getUser().getId();  // 로그인한 clubUser의 ID

        return applications.stream()
                .filter(application -> {
                    DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
                    if (evaluator == null || evaluator.getClubUser() == null) {
                        return false;  // evaluator가 없거나 clubUser가 없으면 필터링
                    }
                    return evaluator.getStage().name().equals(stage)
                            && (request.getGroupName() == null || (evaluator.getGroup() != null && evaluator.getGroup().getName().equals(request.getGroupName())))
                            && evaluator.getClubUser().getUser().getId().equals(currentClubUserId);
                })
                .map(application -> mapToResponse(application, recruitId))  // recruitId 전달
                .sorted((response1, response2) -> {
                    if ("newest".equals(request.getSortOrder())) {
                        return response2.getCreatedAt().compareTo(response1.getCreatedAt());
                    } else if ("oldest".equals(request.getSortOrder())) {
                        return response1.getCreatedAt().compareTo(response2.getCreatedAt());
                    }
                    return 0;  // 정렬 안 정했을 때
                })
                .collect(Collectors.toList());
    }

    // 평가 전 상태 리스트 반환
    public List<DocumentEvaluationResponse> getPendingEvaluations(Long recruitId, DocumentEvaluationRequest request, CustomUserDetails currentUser) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);
        return filterAndSort(applications, request, "BEFORE", currentUser, recruitId);
    }

    // 평가 중 상태와 편집 가능한 상태 리스트를 반환
    public Map<String, List<DocumentEvaluationResponse>> getEvaluationsInProgressOrEditable(Long recruitId, DocumentEvaluationRequest request, CustomUserDetails currentUser) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // "ING" 상태 리스트 반환
        List<DocumentEvaluationResponse> ingList = filterAndSort(applications, request, "ING", currentUser, recruitId);

        // "EDITABLE" 상태 리스트 반환
        List<DocumentEvaluationResponse> editableList = filterAndSort(applications, request, "EDITABLE", currentUser, recruitId);

        Map<String, List<DocumentEvaluationResponse>> response = new HashMap<>();
        response.put("ING", ingList);
        response.put("EDITABLE", editableList);

        return response;
    }

    // 평가 후 상태 리스트 반환
    public List<DocumentEvaluationResponse> getEvaluationsAfter(
            Long recruitId,
            DocumentEvaluationRequest request,
            CustomUserDetails currentUser) {

        ensureRecruitExists(recruitId); // 모집 공고가 존재하는지 확인
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // READABLE 상태 필터링
        List<DocumentEvaluationResponse> readableList = filterAndSort(applications, request, "READABLE", currentUser, recruitId);

        // EDITABLE 상태 필터링
        List<DocumentEvaluationResponse> editableList = filterAndSort(applications, request, "EDITABLE", currentUser, recruitId);

        // 결과 합치기
        List<DocumentEvaluationResponse> combinedList = new ArrayList<>();
        combinedList.addAll(readableList);
        combinedList.addAll(editableList);

        // 날짜별 정렬 (newest 또는 oldest 기준)
        combinedList.sort((response1, response2) -> {
            if ("newest".equals(request.getSortOrder())) {
                return response2.getCreatedAt().compareTo(response1.getCreatedAt());
            } else if ("oldest".equals(request.getSortOrder())) {
                return response1.getCreatedAt().compareTo(response2.getCreatedAt());
            }
            return 0; // 기본: 정렬하지 않음
        });

        return combinedList;
    }

    // 평가 후에서 평가 완료로 이동
    public void updateStagesToAfter(Long recruitId, CustomUserDetails currentUser) {
        List<Group> groups = groupRepository.findByRecruitId(recruitId);

        for (Group group : groups) {
            if (group.isCommon()) {
                // 공통 그룹 처리
                handleCommonGroup(recruitId, group);
            } else {
                // 비공통 그룹 처리
                handleSpecificGroups(group.getId());
            }
        }
    }

    // 평가 완료 불러오기
    public Map<String, List<DocumentEvaluationWithStatusResponse>> getCompletedEvaluations(Long recruitId, DocumentEvaluationRequest request, CustomUserDetails currentUser) {
        ensureRecruitExists(recruitId);

        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // PASS 상태
        List<DocumentEvaluationWithStatusResponse> passedResponses = applications.stream()
                .filter(app -> EvaluateStatus.PASS.equals(app.getState()))
                .map(app -> mapToResponseWithStatus(app, recruitId))
                .collect(Collectors.toList());

        // FAIL 상태
        List<DocumentEvaluationWithStatusResponse> failedResponses = applications.stream()
                .filter(app -> EvaluateStatus.FAIL.equals(app.getState()))
                .map(app -> mapToResponseWithStatus(app, recruitId))
                .collect(Collectors.toList());

        // 정렬 및 필터링 처리
        List<DocumentEvaluationWithStatusResponse> filteredPassedResponses = filterAndSortByRequest(passedResponses, request);
        List<DocumentEvaluationWithStatusResponse> filteredFailedResponses = filterAndSortByRequest(failedResponses, request);

        // 결과 맵핑
        Map<String, List<DocumentEvaluationWithStatusResponse>> result = new HashMap<>();
        result.put("PASS", filteredPassedResponses);
        result.put("FAIL", filteredFailedResponses);

        return result;
    }

    // 상태와 합격 여부를 포함한 response 생성
    private DocumentEvaluationWithStatusResponse mapToResponseWithStatus(Application app, Long recruitId) {
        DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(app.getId());
        Group group = evaluator != null ? evaluator.getGroup() : null;

        return new DocumentEvaluationWithStatusResponse(
                app.getId(),
                app.getState().name(),                               // 상태를 문자열로 반환
                app.getUser().getName(),                             // 사용자 이름
                app.getUser().getPhone(),                            // 사용자 전화번호
                group != null ? group.getName() : null,              // 그룹명 (Group이 있으면 그 이름, 없으면 null)
                EvaluateStatus.PASS.equals(app.getState()),         // 합격 여부 (true/false)
                app.getCreatedAt()                                   // 지원서 제출일
        );
    }

    // 평가 완료 관련 필터링 및 정렬
    private List<DocumentEvaluationWithStatusResponse> filterAndSortByRequest(List<DocumentEvaluationWithStatusResponse> responses, DocumentEvaluationRequest request) {
        return responses.stream()
                .filter(response -> request.getGroupName() == null || request.getGroupName().equals(response.getGroupName()))
                .sorted((r1, r2) -> {
                    if ("newest".equals(request.getSortOrder())) {
                        return r2.getCreatedAt().compareTo(r1.getCreatedAt());
                    } else if ("oldest".equals(request.getSortOrder())) {
                        return r1.getCreatedAt().compareTo(r2.getCreatedAt());
                    }
                    return 0;
                })
                .collect(Collectors.toList());
    }


    // 공통 그룹 처리
    private void handleCommonGroup(Long recruitId, Group group) {
        // 관련된 DocumentEvaluator 가져오기
        List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByRecruitId(recruitId)
                .stream()
                .filter(evaluator -> evaluator.getStage() == Stage.READABLE || evaluator.getStage() == Stage.EDITABLE)
                .toList();

        // Evaluator로부터 Application 조회 및 점수 순 정렬
        List<Application> applications = evaluators.stream()
                .map(DocumentEvaluator::getApplication)
                .sorted(Comparator.comparingInt(Application::getScore).reversed())
                .toList();

        int numDoc = group.getNumDoc(); // PASS로 설정할 개수
        for (int i = 0; i < applications.size(); i++) {
            if (i < numDoc) {
                applications.get(i).setState(EvaluateStatus.PASS); // 상위 numDoc 개수는 PASS
            } else {
                applications.get(i).setState(EvaluateStatus.FAIL); // 나머지는 FAIL
            }
        }

        applicationRepository.saveAll(applications);
    }

    // 비공통 그룹 처리
    private void handleSpecificGroups(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 관련된 DocumentEvaluator 가져오기
        List<DocumentEvaluator> evaluators = documentEvaluatorRepository.findByGroupId(groupId)
                .stream()
                .filter(evaluator -> evaluator.getStage() == Stage.READABLE || evaluator.getStage() == Stage.EDITABLE)
                .toList();

        // Evaluator로부터 Application 조회 및 점수 순 정렬
        List<Application> applications = evaluators.stream()
                .map(DocumentEvaluator::getApplication)
                .sorted(Comparator.comparingInt(Application::getScore).reversed())
                .toList();

        int numDoc = group.getNumDoc(); // PASS로 설정할 개수
        for (int i = 0; i < applications.size(); i++) {
            if (i < numDoc) {
                applications.get(i).setState(EvaluateStatus.PASS); // 상위 numDoc 개수는 PASS
            } else {
                applications.get(i).setState(EvaluateStatus.FAIL); // 나머지는 FAIL
            }
        }

        applicationRepository.saveAll(applications);
    }

    // Response 변환
    private DocumentEvaluationResponse mapToResponse(Application application, Long recruitId) {
        User user = application.getUser();

        // 평가할 전체 운영진 수 가져오기
        int totalEvaluableClubUsers = documentEvaluatorRepository.countUniqueClubUserIdsByRecruitIdAndApplicationId(recruitId, application.getId());

        // 문서 평가자 정보 가져오기
        DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
        Group group = evaluator != null ? evaluator.getGroup() : null;

        return new DocumentEvaluationResponse(
                evaluator != null ? evaluator.getStage().name() : null,          // evaluationStage
                user.getName(),                                                  // applicantName
                user.getPhone(),                                                 // applicantPhone
                group != null ? group.getName() : null,                          // groupName
                application.getNumClubUser() + "/" + totalEvaluableClubUsers,   // applicationNumClubUser
                application.getCreatedAt()                                        // createdAt 값 추가
        );
    }

    // 평가 완료 불러오기
    public void completeDocumentEvaluation(Long recruitId) {
        // 리크루팅이 존재하는지 확인
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅이 존재하지 않습니다."));

        // recruitId와 관련된 모든 Application을 가져옴
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // 모든 지원서 상태를 PASS 또는 FAIL로 업데이트
        applications.forEach(application -> {
            if (application.getState() == null) {
                // 상태가 null일 경우 예시로 PASS 상태로 설정 (상태 로직에 따라 수정 가능)
                application.setState(EvaluateStatus.PASS);
            }
            applicationRepository.save(application); // 상태 저장
        });

        // 리크루팅의 currentStage를 DOC_PASS로 업데이트
        recruit.setCurrentStage(CurrentStage.DOC_PASS);
        recruitRepository.save(recruit); // 변경된 리크루팅 저장
    }

    /// 평가 완료
    public void completeDocument2Evaluation(Long recruitId, List<DocumentEvaluationCompleteRequest> evaluations) {
        // 리크루팅이 존재하는지 확인
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(() -> new IllegalArgumentException("리크루팅이 존재하지 않습니다."));

        // 각 평가를 처리
        for (DocumentEvaluationCompleteRequest evaluation : evaluations) {
            Application application = applicationRepository.findById(evaluation.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 지원서를 찾을 수 없습니다."));

            // 지원서의 리크루팅 정보가 올바른지 확인
            if (!application.getRecruit().getId().equals(recruitId)) {
                throw new IllegalArgumentException("리크루팅 ID가 일치하지 않습니다.");
            }

            // 상태 업데이트
            application.setState(EvaluateStatus.valueOf(evaluation.getState().toUpperCase()));
            applicationRepository.save(application); // 상태 저장
        }

        // 리크루팅의 currentStage를 DOC_PASS로 업데이트
        recruit.setCurrentStage(CurrentStage.DOC_PASS);
        recruitRepository.save(recruit); // 변경된 리크루팅 저장
    }

    // 이의제기 중으로 상태 변경
    public void updateApplicationStateToObjection(Long recruitId, Long applicationId, CustomUserDetails currentUser) {
        // recruitId와 applicationId를 사용해 해당 지원서 찾기
        Application application = applicationRepository.findByRecruitIdAndId(recruitId, applicationId)
                .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));

        // 상태를 OBJECTION으로 변경
        application.setState(EvaluateStatus.OBJECTION);

        // 변경 사항 저장
        applicationRepository.save(application);
    }

    // 지원서 상태 업데이트 및 지원자 정보 반환
    public DocumentEvaluation2Response evaluateApplication(Long recruitId, Long applicationId, String result) {
        ensureRecruitExists(recruitId);

        // 지원서 찾기
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("지원서를 찾을 수 없습니다."));

        // 결과에 따라 상태 변경 (PASS or FAIL)
        if ("PASS".equalsIgnoreCase(result)) {
            application.setState(EvaluateStatus.PASS);
        } else if ("FAIL".equalsIgnoreCase(result)) {
            application.setState(EvaluateStatus.FAIL);
        } else {
            throw new IllegalArgumentException("결과 값은 'PASS' 또는 'FAIL'이어야 합니다.");
        }

        // 지원자 정보 (이름, 전화번호) 가져오기
        User user = application.getUser();
        String applicantName = user.getName();
        String applicantPhone = user.getPhone();

        // 그룹명 가져오기
        DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(applicationId);
        Group group = evaluator != null ? evaluator.getGroup() : null;
        String groupName = group != null ? group.getName() : null;

        // 상태 저장
        applicationRepository.save(application);

        // 응답 객체 생성
        DocumentEvaluation2Response response = new DocumentEvaluation2Response(
                applicantName,
                groupName,
                applicantPhone,
                result
        );

        return response;
    }

    // ---

    // 서류 평가
    @Transactional
    public DocumentEvaluation3Response evaluateDocument(Long recruitId, Long applicationId, DocumentEvaluation3Request request) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid application ID"));

        DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(applicationId);
        if (evaluator == null) {
            evaluator = DocumentEvaluator.builder()
                    .application(application)
                    .stage(Stage.EDITABLE)
                    .score(0)
                    .comment(null)
                    .build();
            documentEvaluatorRepository.save(evaluator);
        }

        int totalScore = 0;
        for (DocumentEvaluation3Request.CriteriaEvaluation criteriaEvaluation : request.getCriteriaEvaluations()) {
            DocumentCriteria criteria = documentCriteriaRepository.findById(criteriaEvaluation.getCriteriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid criteria ID"));

            DocumentEvalScore evalScore = new DocumentEvalScore();
            evalScore.setDocumentEvaluator(evaluator);
            evalScore.setDocumentCriteria(criteria);
            evalScore.setScore(criteriaEvaluation.getScore());
            totalScore += criteriaEvaluation.getScore();

            documentEvalScoreRepository.save(evalScore);
        }

        evaluator.setScore(totalScore);
        evaluator.setComment(request.getComment());
        documentEvaluatorRepository.save(evaluator);

        application.setNumClubUser((application.getNumClubUser() == null ? 0 : application.getNumClubUser()) + 1);
        int newAverageScore = (application.getScore() == null ? totalScore :
                (application.getScore() * (application.getNumClubUser() - 1) + totalScore) / application.getNumClubUser());
        application.setScore(newAverageScore);

        applicationRepository.save(application);

        return new DocumentEvaluation3Response(applicationId, totalScore, request.getComment(), "UPDATED");
    }
}
