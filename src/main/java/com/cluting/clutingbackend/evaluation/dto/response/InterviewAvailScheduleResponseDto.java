package com.cluting.clutingbackend.evaluation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class InterviewAvailScheduleResponseDto {
    private Map<String, Schedule> schedules; // 그룹이름 : schedule

    @Data
    @Builder
    public static class Schedule {
        private Map<String, Composed> dates; // 날짜 : compose
    }

    @Data
    @Builder
    public static class Composed {
        private LocalDateTime time;
        private List<Participant> interviewers;
        private List<Participant> applicants;
    }

    @Data
    @Builder
    public static class Participant {
        private Long id; // 운영진이면 clubUserId, 지원자면 userId
        private String name; // 사용자 이름
        private String groupName;
    }
}
