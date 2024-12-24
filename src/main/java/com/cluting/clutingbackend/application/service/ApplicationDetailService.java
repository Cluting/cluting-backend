package com.cluting.clutingbackend.application.service;


import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.dto.response.ApplicantProfileResponseDto;
import com.cluting.clutingbackend.application.dto.response.ApplicationDetailResponseDto;
import com.cluting.clutingbackend.application.dto.response.DocumentAnswerResponseDto;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.application.repository.DocumentAnswerRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationDetailService {

    private final ApplicationRepository applicationRepository;
    private final DocumentAnswerRepository documentAnswerRepository;
    private final UserRepository userRepository;

    public ApplicationDetailResponseDto getApplicationDetail(Long userId, Long recruitId) {
        // 1. 해당 공고에 지원한 지원서 조회
        Application application = applicationRepository.findByUserIdAndRecruitId(userId, recruitId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공고에 대한 지원서를 찾을 수 없습니다."));

        // 2. 지원서 ID로 서류 답변과 질문 가져오기
        List<DocumentAnswerResponseDto> answers = documentAnswerRepository.findByApplicationId(application.getId())
                .stream()
                .map(answer -> new DocumentAnswerResponseDto(
                        answer.getDocumentQuestion().getContent(),
                        answer.getContent()
                ))
                .collect(Collectors.toList());

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User Not Found!"));


        // 3. 응답 DTO 생성
        return new ApplicationDetailResponseDto(
                application.getId(),
                application.getRecruit().getTitle(),
                application.getUser().getName(),
                application.getUser().getEmail(),
                answers,
                ApplicantProfileResponseDto.builder()
                        .name(user.getName())
                        .phoneNum(user.getPhone())
                        .addr(user.getLocation())
                        .university(user.getSchool())
                        .major(user.getMajor())
                        .doubleMajor(user.getDoubleMajor())
                        .studentStatus(user.getStudentStatus())
                        .semester(user.getSemester())
                        .build()
        );
    }
}
