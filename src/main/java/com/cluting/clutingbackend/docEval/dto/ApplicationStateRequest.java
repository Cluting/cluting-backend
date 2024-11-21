package com.cluting.clutingbackend.docEval.dto;

import com.cluting.clutingbackend.plan.domain.Application;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStateRequest {
    private String name;   // 지원자의 이름
    private String phone;  // 지원자의 전화번호
    private Application.State state; // 새로운 상태
}
