package com.cluting.clutingbackend.docEval.service;

import com.cluting.clutingbackend.docEval.dto.ApplicationResponse;
import com.cluting.clutingbackend.docEval.repository.Application2Repository;
import com.cluting.clutingbackend.docEval.repository.Evaluation2Repository;
import com.cluting.clutingbackend.docEval.repository.Evaluator2Repository;
import com.cluting.clutingbackend.docEval.repository.Part2Repository;
import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Application2Service {

    private final Application2Repository applicationRepository;
    private final Evaluation2Repository evaluationRepository;
    private final Part2Repository partRepository;
    private final Evaluator2Repository evaluatorRepository;

    public List<ApplicationResponse> getApplicationList(Long clubId, Long postId, Long clubUserId) {
        // 1. 모집 공고(postId)에 속한 지원서 조회
        List<Application> applications = applicationRepository.findByPostId(postId);

        // 2. 지원서 응답 생성
        List<ApplicationResponse> responses = new ArrayList<>();
        for (Application application : applications) {
            // 지원서 평가 상태
            Application.State state = application.getState();

            // 지원자 이름과 전화번호
            User user = application.getUser();
            String applicantName = user.getName();
            String phone = user.getPhone();

            // 지원한 그룹
            String part = application.getPart();

            // 평가한 운영진 수
            Long evaluatedCount = evaluationRepository.countDistinctClubUsersByApplication(application.getApplicationId());

            // 평가 권한이 있는 운영진 수
            Long totalEvaluatorCount = part != null
                    ? evaluatorRepository.countDistinctClubUsersByPartAndPostId(part, postId)
                    : 0L;

            // 평가 상황 형식
            String evaluationStatus = evaluatedCount + "/" + totalEvaluatorCount;

            // 응답 추가
            responses.add(new ApplicationResponse(state, applicantName, phone, part, evaluationStatus));
        }

        return responses;
    }
}
