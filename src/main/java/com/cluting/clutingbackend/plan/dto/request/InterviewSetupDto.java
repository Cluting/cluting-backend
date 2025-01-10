package com.cluting.clutingbackend.plan.dto.request;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterviewSetupDto {

    private Integer interviewer; // 면접관 수
    private Integer interviewee; // 면접자 수
    private Integer interviewDuration; // 면접소요시간
    private String groupName; // 면접관 그룹 선택 (추후 List<String>으로 변경해야 할 듯)

}
