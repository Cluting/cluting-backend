package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationResponse;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
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
}
