package com.cluting.clutingbackend.plan.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSetupDto {

    private Integer interviewer; // 면접관 수
    private Integer interviewee; // 면접자 수
    private Integer interviewDuration; // 면접소요시간

}
