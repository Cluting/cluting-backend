package com.cluting.clutingbackend.docEval.dto;

import com.cluting.clutingbackend.plan.domain.Application;
import lombok.Setter;

@Setter
public class ApplicationDto {
    private Long applicationId;
    private String evaluationStatus;
    private String name;
    private String phone;
    private String group;
    private String evaluatorStatus;

    public ApplicationDto(Application application) {
        this.applicationId = application.getApplicationId();
    }

    // Getters and Setters
}
