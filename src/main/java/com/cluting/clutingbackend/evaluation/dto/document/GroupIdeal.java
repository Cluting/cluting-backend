package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(staticName = "of")
public class GroupIdeal {
    private String question;
    private List<String> ideals;
}
