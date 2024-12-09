package com.cluting.clutingbackend.plan.dto.response;


import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class Plan3ResponseDto {

    private LocalDate DocStart; // 서류합격자발표일
    private LocalDate FinalStart; // 최종합격자발표일
    private LocalDate RecruitStart; // 모집시작일
    private LocalDate RecruitEnd; // 모집종료일
}
