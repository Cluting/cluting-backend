package com.cluting.clutingbackend.evaluation.dto.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DocumentEvaluationWithStatusResponse {
    private Long id; //지원서 id
    private String status;          // 합격 여부
    private String applicantName;  // 지원자 이름
    private String applicantPhone; // 지원자 전화번호
    private String groupName;      // 그룹명
    private boolean isPassed;       // 합격 여부 (true/false)
    private LocalDateTime createdAt; // 지원서 제출일
}
