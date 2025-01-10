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

    private Map<String,List<Long>> groupAndClubUser; // 그룹과 선택한 운영진 ID 반환

}
