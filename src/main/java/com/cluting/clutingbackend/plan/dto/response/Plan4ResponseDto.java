package com.cluting.clutingbackend.plan.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Plan4ResponseDto {

    private Integer interviewer; // 면접관 수
    private Integer interviewee; // 면접자 수
    private Integer interviewDuration; // 면접소요시간

    private Map<String,List<Long>> groupAndClubUser; // 그룹과 선택한 운영진 ID 반환
}
