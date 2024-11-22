package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.plan.domain.TimeSlot;
import com.cluting.clutingbackend.plan.dto.request.InterviewSetupDto;
import com.cluting.clutingbackend.plan.dto.request.TimeSlotAvailabilityDto;
import com.cluting.clutingbackend.plan.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.repository.TimeSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final TimeSlotRepository timeSlotRepository;

    public Interview createInterview(InterviewSetupDto interviewSetupDto){
        Interview interview = Interview.builder()
                .interviewerCount(interviewSetupDto.getInterviewerCount())
                .intervieweeCount(interviewSetupDto.getIntervieweeCount())
                .duration(interviewSetupDto.getDuration())
                .build();
        return interviewRepository.save(interview);
    }

    public void saveAvailability(Long interviewId, TimeSlotAvailabilityDto dto) {
        dto.getSelectedSlots().forEach((timeSlot, isAvailable) -> {
            if (isAvailable) {
                Optional<TimeSlot> existingSlot = timeSlotRepository.findByInterviewId(interviewId).stream()
                        .filter(slot -> slot.getTimeSlot().equals(timeSlot))
                        .findFirst();
                TimeSlot slot = existingSlot.orElseGet(() -> TimeSlot.builder()
                        .timeSlot(timeSlot)
                        .interview(Interview.builder().id(interviewId).build())
                        .availableInterviewers(new ArrayList<>())
                        .build());
                slot.getAvailableInterviewers().add(dto.getUserId());
                timeSlotRepository.save(slot);
            }
        });
    }

    // 시간대별 가능한 운영진 조회
    public Map<LocalDateTime, List<Long>> getAvailableInterviewers(Long interviewId) {
        List<TimeSlot> slots = timeSlotRepository.findByInterviewId(interviewId);
        Map<LocalDateTime, List<Long>> response = new HashMap<>();
        for (TimeSlot slot : slots) {
            response.put(slot.getTimeSlot(), slot.getAvailableInterviewers());
        }
        return response;
    }


}
