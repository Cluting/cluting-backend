package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.plan.domain.TimeSlot;
import com.cluting.clutingbackend.plan.dto.request.InterviewAssignmentDto;
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
    public Map<LocalDateTime, Map<String, Object>> getAvailableInterviewers(Long interviewId) {
        List<TimeSlot> slots = timeSlotRepository.findByInterviewId(interviewId);

        Map<LocalDateTime, Map<String, Object>> response = new HashMap<>();
        for (TimeSlot slot : slots) {
            Map<String, Object> slotInfo = new HashMap<>();
            slotInfo.put("availableInterviewers", slot.getAvailableInterviewers());
            slotInfo.put("assigned", slot.getAssigned());
            response.put(slot.getTimeSlot(), slotInfo);
        }
        return response;
    }

    public void assignInterviewers(Long interviewId, InterviewAssignmentDto dto) {
        Interview interview = interviewRepository.findById(interviewId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid interview ID"));

        for (InterviewAssignmentDto.Assignment assignment : dto.getAssignmentList()) {
            TimeSlot timeSlot = timeSlotRepository.findByInterviewIdAndTimeSlot(interviewId, assignment.getTimeslot())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid time slot"));

            timeSlot.setAssigned(true); // 배정 상태로 변경
            timeSlot.setAvailableInterviewers(assignment.getInterviewers()); // 면접관 배정
            timeSlotRepository.save(timeSlot);
        }
    }

}
