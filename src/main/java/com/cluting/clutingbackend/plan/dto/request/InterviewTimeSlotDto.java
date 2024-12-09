package com.cluting.clutingbackend.plan.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewTimeSlotDto {
    private Long clubUserId;              // 운영진 ID
    private List<LocalDateTime> timeSlots; // 면접 가능 시간대 리스트
}
