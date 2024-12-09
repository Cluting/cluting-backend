package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.DocumentEvaluationResponse;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final RecruitRepository recruitRepository;

    // 모집 공고가 존재하는지 확인
    private void ensureRecruitExists(Long recruitId) {
        if (!recruitRepository.existsById(recruitId)) {
            throw new IllegalArgumentException("모집 공고가 존재하지 않습니다.");
        }
    }

    // 문서 변환 메서드
    private DocumentEvaluationResponse mapToResponse(Application application, Long recruitId) {
        User user = application.getUser();

        // 평가할 전체 운영진 수 가져오기
        int totalEvaluableClubUsers = documentEvaluatorRepository.countUniqueClubUserIdsByRecruitIdAndApplicationId(recruitId, application.getId());

        // 문서 평가자 정보 가져오기
        DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
        Group group = evaluator != null ? evaluator.getGroup() : null;

        return new DocumentEvaluationResponse(
                evaluator != null ? evaluator.getStage().name() : null,
                user.getName(),
                user.getPhone(),
                group != null ? group.getName() : null,
                application.getNumClubUser() + "/" + totalEvaluableClubUsers, // 현재 평가한 운영진 수 / 평가할 전체 운영진 수
                application.getCreatedAt() // createdAt 값 추가
        );
    }


    // 평가 전 상태인 서류 평가 리스트 반환 (BEFORE인 상태만 필터링)
    public List<DocumentEvaluationResponse> getPendingEvaluations(Long recruitId) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
                    return evaluator != null && evaluator.getStage().name().equals("BEFORE");
                })
                .map(application -> mapToResponse(application, recruitId))
                .collect(Collectors.toList());
    }

    // 그룹별 서류 평가 리스트 반환 (BEFORE인 상태만 필터링)
    public List<DocumentEvaluationResponse> getDocumentsByGroup(Long recruitId, String groupName) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
                    return evaluator != null && evaluator.getStage().name().equals("BEFORE") &&
                            evaluator.getGroup() != null && evaluator.getGroup().getName().equals(groupName);
                })
                .map(application -> mapToResponse(application, recruitId))
                .collect(Collectors.toList());
    }

    // 최신 서류 평가 리스트 반환 (BEFORE인 상태만 필터링)
    public List<DocumentEvaluationResponse> getDocumentsByNewest(Long recruitId) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
                    return evaluator != null && evaluator.getStage().name().equals("BEFORE");
                })
                .map(application -> mapToResponse(application, recruitId)) // recruitId 전달
                .sorted(Comparator.comparing(DocumentEvaluationResponse::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

    // 오래된 서류 평가 리스트 반환 (BEFORE인 상태만 필터링)
    public List<DocumentEvaluationResponse> getDocumentsByOldest(Long recruitId) {
        ensureRecruitExists(recruitId);
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        return applications.stream()
                .filter(application -> {
                    DocumentEvaluator evaluator = documentEvaluatorRepository.findByApplicationId(application.getId());
                    return evaluator != null && evaluator.getStage().name().equals("BEFORE");
                })
                .map(application -> mapToResponse(application, recruitId)) // recruitId 전달
                .sorted(Comparator.comparing(DocumentEvaluationResponse::getCreatedAt))
                .collect(Collectors.toList());
    }

}
