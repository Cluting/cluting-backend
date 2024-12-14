package com.cluting.clutingbackend.evaluation.dto.interview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EvaluateUserResponse {
    private String name;
    private String phone;
    private String groupName;
    private String result;
}
