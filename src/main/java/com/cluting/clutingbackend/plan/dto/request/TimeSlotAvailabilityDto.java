package com.cluting.clutingbackend.plan.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class TimeSlotAvailabilityDto {
    private Long userId;
    private Map<LocalDateTime,Boolean> selectedSlots;
}
