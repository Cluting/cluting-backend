package com.cluting.clutingbackend.plan.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class AssignTimeSlotDto {
    private Map<LocalDateTime, List<Long>> assignedInterviewers; // Key: 시간대, Value: 면접관 ID 리스트
}

