package com.cluting.clutingbackend.interview.service;

import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.global.enums.EvalType;
import com.cluting.clutingbackend.global.enums.QuestionType2;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewAnswer;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import com.cluting.clutingbackend.interview.domain.InterviewQuestion;
import com.cluting.clutingbackend.interview.dto.*;
import com.cluting.clutingbackend.interview.exception.ResourceNotFoundException;
import com.cluting.clutingbackend.interview.repository.InterviewAnswerRepository;
import com.cluting.clutingbackend.interview.repository.InterviewEvaluatorRepository;
import com.cluting.clutingbackend.interview.repository.InterviewQuestionRepository;
import com.cluting.clutingbackend.interview.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final InterviewEvaluatorRepository interviewEvaluatorRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewAnswerRepository interviewAnswerRepository;

    public InterviewDetailsDto getInterviewDetails(Long interviewId) {
        // 1. Interview Time and Date
        LocalDateTime interviewTime = interviewEvaluatorRepository.findFirstByInterviewId(interviewId)
                .map(InterviewEvaluator::getInterviewTime)
                .orElseThrow(() -> new IllegalArgumentException("Interview time not found"));

        String dayOfWeek = interviewTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        // 2. Interviewee Information
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Interview not found"));

        Application application = interview.getApplication();
        User user = application.getUser();

        String groupName = getGroupName(interviewId);

        IntervieweeInfoDto intervieweeInfo = new IntervieweeInfoDto(
                user.getName(),
                user.getProfile(),
                groupName,
                user.getSchool(),
                user.getMajor(),
                user.getSemester().getDescription()
        );

        // 3. Questions and Answers
        List<QuestionAnswerDto> commonQuestions = getQuestionsAndAnswers(interviewId, QuestionType2.COMMON);
        List<QuestionAnswerDto> groupQuestions = getQuestionsAndAnswers(interviewId, QuestionType2.PART_SPECIFIC);
        Map<Long, List<QuestionAnswerDto>> personalQuestions = getPersonalQuestions(interviewId);

        return new InterviewDetailsDto(interviewTime, dayOfWeek, intervieweeInfo, commonQuestions, groupQuestions, personalQuestions);
    }

    private String getGroupName(Long interviewId) {
        return interviewEvaluatorRepository.findFirstByInterviewId(interviewId)
                .map(evaluator -> {
                    Group group = evaluator.getGroup();
                    if (group.isCommon()) {
                        return group.getEvalType() == EvalType.INTERVIEW ? group.getName() : "Common Group";
                    } else {
                        return group.getName();
                    }
                })
                .orElse("No Group");
    }

    private List<QuestionAnswerDto> getQuestionsAndAnswers(Long interviewId, QuestionType2 type) {
        return interviewQuestionRepository.findByInterviewIdAndType(interviewId, type).stream()
                .map(question -> {
                    String answerContent = interviewAnswerRepository.findByInterviewQuestionId(question.getId())
                            .map(InterviewAnswer::getContent)
                            .orElse(null);
                    return new QuestionAnswerDto(question.getContent(), answerContent);
                })
                .collect(Collectors.toList());
    }

    private Map<Long, List<QuestionAnswerDto>> getPersonalQuestions(Long interviewId) {
        List<InterviewQuestion> questions = interviewQuestionRepository.findByInterviewIdAndType(interviewId, QuestionType2.PERSONAL);

        Map<Long, List<QuestionAnswerDto>> result = new HashMap<>();
        for (InterviewQuestion question : questions) {
            Long userId = question.getInterview().getApplication().getUser().getId();
            QuestionAnswerDto qa = new QuestionAnswerDto(
                    question.getContent(),
                    interviewAnswerRepository.findByInterviewQuestionId(question.getId())
                            .map(InterviewAnswer::getContent)
                            .orElse(null)
            );
            result.computeIfAbsent(userId, k -> new ArrayList<>()).add(qa);
        }
        return result;
    }

    @Transactional
    public void saveInterviewAnswers(InterviewAnswerRequestDto requestDto) {
        for (InterviewAnswerDto answerDto : requestDto.getAnswers()) {
            // 1. InterviewQuestion 엔티티 검색
            InterviewQuestion question = interviewQuestionRepository
                    .findByInterviewIdAndIdAndType(
                            requestDto.getInterviewId(),
                            answerDto.getQuestionId(),
                            answerDto.getType()
                    )
                    .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

            // 2. InterviewAnswer 엔티티 저장
            InterviewAnswer answer = InterviewAnswer.builder()
                    .content(answerDto.getContent())
                    .interviewQuestion(question)
                    .build();

            interviewAnswerRepository.save(answer);
        }
    }
}
