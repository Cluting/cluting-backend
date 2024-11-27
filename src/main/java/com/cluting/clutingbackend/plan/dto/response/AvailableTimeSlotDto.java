package com.cluting.clutingbackend.plan.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AvailableTimeSlotDto {
    private LocalDateTime timeSlot; // 시간대
    private List<Long> availableClubUserIds; // 가능한 운영진 ID 리스트
}

