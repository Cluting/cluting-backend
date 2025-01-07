package com.cluting.clutingbackend.application.dto.response;

import com.cluting.clutingbackend.global.enums.EvaluateStatus;

import java.util.List;

public class ApplicationDetailResponseDto {

    private Long applicationId;
    private String postTitle;
    private String userName;
    private String userEmail;
    private List<DocumentAnswerResponseDto> answers;

    private ApplicantProfileResponseDto profileResponseDto;

    public ApplicationDetailResponseDto(Long applicationId, String postTitle, String userName, String userEmail, List<DocumentAnswerResponseDto> answers,ApplicantProfileResponseDto dto) {
        this.applicationId = applicationId;
        this.postTitle = postTitle;
        this.userName = userName;
        this.userEmail = userEmail;
        this.answers = answers;
        this.profileResponseDto = dto;
    }

}
