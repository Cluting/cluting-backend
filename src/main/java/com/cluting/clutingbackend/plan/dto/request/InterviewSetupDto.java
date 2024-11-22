package com.cluting.clutingbackend.plan.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewSetupDto {
    private Integer interviewerCount;
    private Integer intervieweeCount;
    private Integer duration;
}
