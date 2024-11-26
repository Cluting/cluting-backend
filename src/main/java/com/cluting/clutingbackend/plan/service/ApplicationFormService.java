package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.part.Part;
import com.cluting.clutingbackend.part.PartRepository;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.Question;
import com.cluting.clutingbackend.plan.dto.request.ApplicationFormRequestDto;
import com.cluting.clutingbackend.plan.dto.response.ApplicationFormResponseDto;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import com.cluting.clutingbackend.plan.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationFormService {
    private final QuestionRepository questionRepository;
    private final PartRepository partRepository;
    private final PostRepository postRepository;

    public ApplicationFormRequestDto createApplicationForm(Long postId, ApplicationFormRequestDto dto) {
        // 1. Post 객체 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post is not found!"));

        // 2. Post 정보 업데이트
        post.setApplicationTitle(dto.getTitle());
        post.setRequiredPortfolio(dto.getPortfolioRequired());
        postRepository.save(post); // Post 변경사항 저장

        // 3. 질문 저장
        List<Question> questionList = new ArrayList<>();
        for (ApplicationFormRequestDto.QuestionDto questionDto : dto.getQuestions()) {
            Question question = new Question();
            question.setContent(questionDto.getContent());
            question.setType(Question.Type.valueOf(questionDto.getType()));
            question.setDocOrInterview(questionDto.getDocOrInterview());
            question.setCategory(Question.Category.valueOf(questionDto.getCategory()));
            question.setOptions(questionDto.getChoices());
            question.setMultipleChoice(questionDto.getMultipleChoice());

            // Part-specific 질문 처리
            if ("PART_SPECIFIC".equals(questionDto.getCategory()) && questionDto.getPartId() != null) {
                Part part = partRepository.findById(questionDto.getPartId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Part ID"));
                question.setPart(part);
            }

//            question.set(post); // 질문과 모집공고 연결
            questionList.add(question);
        }

        // 4. 저장된 질문 리스트 저장
        questionRepository.saveAll(questionList);

        // 5. DTO 반환
        return ApplicationFormRequestDto.builder()
                .title(post.getApplicationTitle())
                .portfolioRequired(post.isRequiredPortfolio())
                .questions(questionList.stream()
                        .map(question -> ApplicationFormRequestDto.QuestionDto.builder()
                                .content(question.getContent())
                                .type(question.getType().name())
                                .docOrInterview(question.getDocOrInterview())
                                .category(question.getCategory().name())
                                .choices(question.getOptions())
                                .multipleChoice(question.getMultipleChoice())
                                .partId(question.getPart() != null ? question.getPart().getId() : null)
                                .build())
                        .toList())
                .build();
    }




}
