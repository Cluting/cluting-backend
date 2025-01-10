package com.cluting.clutingbackend.plan.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewTimeSlotResponseDto {
    private Long recruitId;                           // 모집 ID
    private List<TimeSlotInfo> timeSlots;             // 시간대 정보 리스트

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotInfo {
        private LocalDateTime timeSlot;               // 면접 시간대
        private boolean isAssigned;                  // 할당 여부
        private List<InterviewerInfo> interviewers;   // 면접관 정보 리스트
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InterviewerInfo {
        private Long id;                              // 면접관 ID
        private String name;                          // 면접관 이름
        private String groupName;                    // 면접관 그룹 이름
    }
}

