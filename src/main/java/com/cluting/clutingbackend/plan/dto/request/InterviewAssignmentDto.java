package com.cluting.clutingbackend.plan.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InterviewAssignmentDto {
    private List<Assignment> assignmentList;

    @Getter
    @Setter
    public static class Assignment{
        private LocalDateTime timeslot;
        private List<Long> interviewers;
    }
}
