package com.cluting.clutingbackend.plan.dto.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
public class TimeSlotAvailabilityDto {
    private Long clubUserId; // 운영진 ID
    private List<TimeSlotDto> timeSlots; // 선택된 시간대 리스트

    @Data
    public static class TimeSlotDto {
        private LocalDate date; // 날짜
        private LocalTime time; // 시간
        private boolean isAvailable; // 해당 시간대의 활성 여부
    }
}
