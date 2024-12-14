package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEvaluationRequest {
    private String groupName;  // 그룹명 필터
    private String sortOrder;  // 최신순 또는 지원순
}
