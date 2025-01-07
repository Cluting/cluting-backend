package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InterviewResponseDTO {
    private String date;
    private List<TimeSlotDTO> timeSlots;

    @Data
    @Builder
    public static class TimeSlotDTO {
        private String time;
        private List<GroupDTO> groups;

        @Data
        @Builder
        public static class GroupDTO {
            private String groupName;
            private List<String> interviewer; // 면접관 이름 리스트
            private List<String> interviewee; // 면접자 이름 리스트
        }
    }
}