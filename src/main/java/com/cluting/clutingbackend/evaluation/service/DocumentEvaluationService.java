package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationResponse;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final RecruitRepository recruitRepository;

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
                        return Comparator.comparing(DocumentEvaluationResponse::getCreatedAt)
                                .reversed()
                                .compare(response1, response2);
                    } else if ("oldest".equals(request.getSortOrder())) {
                        return Comparator.comparing(DocumentEvaluationResponse::getCreatedAt)
                                .compare(response1, response2);
                    }
                    return 0;
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
