package com.cluting.clutingbackend.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DocumentPrepResponseDto {
    private String portfolio;
    private Map<String, List<String>> time;
}
