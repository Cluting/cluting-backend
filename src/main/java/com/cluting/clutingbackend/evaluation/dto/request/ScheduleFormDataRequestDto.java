package com.cluting.clutingbackend.evaluation.dto.request;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ScheduleFormDataRequestDto {
    private Map<Long, Group> groups;

    @Data
    public static class Group {
        private String groupName;
        private Map<String, DateSchedules> dates;
    }

    @Data
    public static class DateSchedules {
        private List<Schedule> schedules;
    }

    @Data
    public static class Schedule {
        private String time;
        private List<Long> interviewers;
        private List<Long> applicants;
    }
}

