package com.cluting.clutingbackend.plan.dto.request;

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
public class InterviewerAssignedDto {

    private List<TimeSlotAssignment> timeSlotAssignments; // 시간대별 면접관 배정 목록

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimeSlotAssignment {
        private LocalDateTime timeSlot; // 면접 시간대
        private List<Long> interviewerIds; // 면접관 ID 리스트
    }
}

