package com.cluting.clutingbackend.global.security;

import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;


import java.util.Collections;
import java.util.List;

public class PermissionLevelConverter implements AttributeConverter<List<PermissionLevel>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<PermissionLevel> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting list to JSON string", e);
        }
    }

    @Override
    public List<PermissionLevel> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            // 데이터베이스 값이 null 또는 빈 문자열일 경우 빈 리스트 반환
            return Collections.emptyList();
        }
        try {
            System.out.println("permission level -> " + dbData);
            return objectMapper.readValue(dbData, new TypeReference<List<PermissionLevel>>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON string to list", e);
        }
    }
}
