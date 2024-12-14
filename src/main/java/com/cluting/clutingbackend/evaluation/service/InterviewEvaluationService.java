package com.cluting.clutingbackend.evaluation.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.evaluation.dto.GroupResponse;
import com.cluting.clutingbackend.evaluation.dto.interview.InterviewEvaluationRequest;
import com.cluting.clutingbackend.evaluation.dto.interview.InterviewEvaluationResponse;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewEvaluationService {

    private final ApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final GroupRepository groupRepository;

    public List<InterviewEvaluationResponse> getInterviewEvaluations(Long recruitId, CustomUserDetails currentUser, InterviewEvaluationRequest request) {
        Long currentClubUserId = currentUser.getUser().getId();

        // Step 1: 모집 공고 ID로 Application 조회
        List<Application> applications = applicationRepository.findByRecruitId(recruitId);

        // Step 2: Application ID로 Interview 조회
        List<Interview> interviews = interviewRepository.findByApplicationIdIn(
                applications.stream().map(Application::getId).collect(Collectors.toList()));

        // Step 3: Interview ID로 InterviewEvaluator 조회 및 단계별 분리
        List<InterviewEvaluator> evaluators = interviewEvaluatorRepository.findByInterviewIdIn(
                interviews.stream().map(Interview::getId).collect(Collectors.toList()));

        return evaluators.stream()
                .filter(evaluator -> evaluator.getClubUser() != null
                        && evaluator.getClubUser().getUser().getId().equals(currentClubUserId)
                        && (request.getGroupName() == null ||
                        (evaluator.getGroup() != null &&
                                evaluator.getGroup().getName().equals(request.getGroupName()))))
                .map(evaluator -> mapToResponse(evaluator, request.getSortOrder()))
                .sorted((resp1, resp2) -> sortResponses(resp1, resp2, request.getSortOrder()))
                .collect(Collectors.toList());
    }

    private InterviewEvaluationResponse mapToResponse(InterviewEvaluator evaluator, String sortOrder) {
        Interview interview = evaluator.getInterview();
        Application application = interview.getApplication();
        User applicant = application.getUser();

        int totalEvaluators = interviewEvaluatorRepository.countDistinctByInterviewId(interview.getId());
        int currentEvaluators = interview.getNumClubUser() != null ? interview.getNumClubUser() : 0;

        return InterviewEvaluationResponse.builder()
                .stage(evaluator.getStage().name())
                .applicantName(applicant.getName())
                .applicantPhone(applicant.getPhone())
                .groupName(evaluator.getGroup() != null ? evaluator.getGroup().getName() : "N/A")
                .evaluationStatus(currentEvaluators + "/" + totalEvaluators)
                .build();
    }

    private int sortResponses(InterviewEvaluationResponse resp1, InterviewEvaluationResponse resp2, String sortOrder) {
        if ("newest".equals(sortOrder)) {
            return resp2.getStage().compareTo(resp1.getStage());
        } else if ("oldest".equals(sortOrder)) {
            return resp1.getStage().compareTo(resp2.getStage());
        }
        return 0;
    }


    public List<GroupResponse> getGroupsByRecruitId(Long recruitId) {
        // Group 엔티티에서 recruitId로 그룹 조회
        List<Group> groups = groupRepository.findAllByRecruitId(recruitId);

        // GroupResponse로 매핑
        return groups.stream()
                .map(group -> new GroupResponse(group.getId(), group.getName()))
                .toList();
    }
}
