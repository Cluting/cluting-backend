package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DocumentEvaluation2Response {
    private String name;
    private String groupName;
    private String phone;
    private String result;
}
