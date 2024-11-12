package com.cluting.clutingbackend.recruitingHome.response;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import com.cluting.clutingbackend.plan.domain.Todo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecruitingHomeResponse {
    private String clubName;
    private Post.CurrentStage currentStage;
    private Boolean isInterview;
    private Integer generation;
    private List<RecruitSchedule> recruitSchedules;
    private List<ClubUser> clubUsers;
    private List<Todo> todos;

    // Constructor
    public RecruitingHomeResponse(String clubName, Post.CurrentStage currentStage, Boolean isInterview, Integer generation,
                                  List<RecruitSchedule> recruitSchedules, List<ClubUser> clubUsers, List<Todo> todos) {
        this.clubName = clubName;
        this.currentStage = currentStage;
        this.isInterview = isInterview;
        this.generation = generation;
        this.recruitSchedules = recruitSchedules;
        this.clubUsers = clubUsers;
        this.todos = todos;
    }
}