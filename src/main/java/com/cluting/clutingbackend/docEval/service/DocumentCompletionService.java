package com.cluting.clutingbackend.docEval.service;

import com.cluting.clutingbackend.docEval.dto.ApplicationDto;
import com.cluting.clutingbackend.docEval.repository.Application2Repository;
import com.cluting.clutingbackend.docEval.repository.Evaluation2Repository;
import com.cluting.clutingbackend.docEval.repository.Evaluator2Repository;
import com.cluting.clutingbackend.evaluate.domain.Evaluation;
import com.cluting.clutingbackend.evaluate.domain.Evaluator;
import com.cluting.clutingbackend.part.PartRepository;
import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.part.Part;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentCompletionService {

    private final Application2Repository applicationRepository;
    private final Evaluation2Repository evaluationRepository;
    private final PartRepository partRepository;
    private final Evaluator2Repository evaluatorRepository;

    public DocumentCompletionService(Application2Repository applicationRepository,
                                     Evaluation2Repository evaluationRepository,
                                     PartRepository partRepository,
                                     Evaluator2Repository evaluatorRepository) {
        this.applicationRepository = applicationRepository;
        this.evaluationRepository = evaluationRepository;
        this.partRepository = partRepository;
        this.evaluatorRepository = evaluatorRepository;
    }

    public Map<String, List<ApplicationDto>> getCompletedApplications(Long postId) {
        List<Application> applications = applicationRepository.findByPostId(postId);

        List<ApplicationDto> passApplications = new ArrayList<>();
        List<ApplicationDto> failApplications = new ArrayList<>();

        for (Application application : applications) {
            ApplicationDto applicationDto = new ApplicationDto(application);

            // 합격/불합격 분리
            if (application.getState() == Application.State.PASS) {
                passApplications.add(applicationDto);
            } else if (application.getState() == Application.State.FAIL) {
                failApplications.add(applicationDto);
            }
        }

        Map<String, List<ApplicationDto>> response = new HashMap<>();
        response.put("pass", passApplications);
        response.put("fail", failApplications);
        return response;
    }

    // 필수적인 데이터들을 포함하는 ApplicationDto 객체 생성
    private ApplicationDto createApplicationDto(Application application, Long postId) {
        ApplicationDto applicationDto = new ApplicationDto(application);

        // 지원서 평가 상태
        applicationDto.setEvaluationStatus(application.getState().name());

        // 지원자 이름, 전화번호
        User user = application.getUser();
        applicationDto.setName(user.getName());
        applicationDto.setPhone(user.getPhone());

        // 지원한 그룹 (part)
        applicationDto.setGroup(application.getPart());

        // 운영진들의 평가 상황
        int evaluatorCount = getEvaluatorCount(application.getId());
        int totalEvaluators = getTotalEvaluatorsForPart(postId, application.getPart());

        applicationDto.setEvaluatorStatus(String.format("%d/%d", evaluatorCount, totalEvaluators));

        return applicationDto;
    }

    // 해당 Application에 대한 평가한 운영진 수를 구하는 로직
    private int getEvaluatorCount(Long applicationId) {
        List<Evaluation> evaluations = evaluationRepository.findByApplication_Id(applicationId);
        Set<Long> evaluatorIds = evaluations.stream()
                .map(evaluation -> evaluation.getClubUser().getId())
                .collect(Collectors.toSet());
        return evaluatorIds.size();
    }

    // 해당 파트에 평가 권한이 있는 운영진 수를 구하는 로직
    private int getTotalEvaluatorsForPart(Long postId, String part) {
        List<Part> parts = partRepository.findByPostId(postId);
        Set<Long> evaluatorIds = new HashSet<>();
        for (Part p : parts) {
            if (p.getName().equals(part)) {
                List<Evaluator> evaluators = evaluatorRepository.findByPart_Id(p.getId());
                evaluatorIds.addAll(
                        evaluators.stream()
                                .flatMap(evaluator -> evaluator.getEvaluators().stream()) // 각 Evaluator의 evaluators 리스트를 평탄화
                                .map(ClubUser::getId) // ClubUser의 ID를 추출
                                .collect(Collectors.toSet()) // 중복 제거 후 Set으로 변환
                );
            }
        }
        return evaluatorIds.size();
    }
}