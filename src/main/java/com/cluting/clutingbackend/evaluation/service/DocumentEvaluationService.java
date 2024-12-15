package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluateResultResponseDto;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluateResultsResponseDto;
import com.cluting.clutingbackend.evaluation.dto.response.DocumentEvaluationResponse;
import com.cluting.clutingbackend.global.enums.EvaluateStatus;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final RecruitRepository recruitRepository;
    private final GroupRepository groupRepository;

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

    // 설정한 서류 합격자 수 조회
    @Transactional(readOnly = true)
    public RecruitNumResponseDto findDocRecruit(Long recruitId) {
        Map<String, Integer> groupMap = new HashMap<>();
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        int totalNum = 0;
        for (Group group : groups) {
            totalNum += group.getNumRecruit();
            if (!group.isCommon()) {
                groupMap.put(group.getName(), group.getNumDoc());
            }
        }

        return new RecruitNumResponseDto(totalNum, groupMap);
    }

    // 서류 합격/불합격 조회
    @Transactional(readOnly = true)
    public DocumentEvaluateResultsResponseDto findPassAndFail(Long recruitId, SortType sortType) {
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        Map<String, Integer> groupCountMap = new HashMap<>();
        List<DocumentEvaluateResultResponseDto> passed = new ArrayList<>();
        List<DocumentEvaluateResultResponseDto> failed = new ArrayList<>();

        applications.forEach(application -> {
            DocumentEvaluator documentEvaluator = documentEvaluatorRepository.findByApplicationId(application.getId());

            String groupName = documentEvaluator.getGroup().getName();
            groupCountMap.put(groupName, groupCountMap.getOrDefault(groupName, 0) + 1);

            DocumentEvaluateResultResponseDto dto = DocumentEvaluateResultResponseDto.toDto(
                    application,
                    documentEvaluator.getStage(),
                    application.getState() == EvaluateStatus.PASS ? "합격" : "불합격"
            );

            if (application.getState() == EvaluateStatus.PASS) {
                passed.add(dto);
            } else if (application.getState() == EvaluateStatus.FAIL) {
                failed.add(dto);
            }
        });
        sortAndAssignRank(passed);
        sortAndAssignRank(failed);

        if (sortType == SortType.NEWEST) {
            passed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getCreatedAt).reversed());
            failed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getCreatedAt).reversed());
        } else if (sortType == SortType.OLDEST) {
            passed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getCreatedAt));
            failed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getCreatedAt));
        } else if (sortType == SortType.INORDER) {
            passed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getName));
            failed.sort(Comparator.comparing(DocumentEvaluateResultResponseDto::getName));
        } else {
            throw new IllegalArgumentException("정의되지 않은 정렬 방식 입니다.");
        }

        return DocumentEvaluateResultsResponseDto.builder()
                .passedCnt(passed.size())
                .byGroup(groupCountMap)
                .passed(passed)
                .failedCnt(failed.size())
                .failed(failed)
                .build();
    }

    private void sortAndAssignRank(List<DocumentEvaluateResultResponseDto> list) {
        list.sort((o1, o2) -> Integer.compare(o2.getScore(), o1.getScore())); // 내림차순 정렬
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i + 1); // 1부터 시작하는 순위 설정
        }
    }
}
