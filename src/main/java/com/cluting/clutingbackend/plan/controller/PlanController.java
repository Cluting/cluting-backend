package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.dto.request.*;
import com.cluting.clutingbackend.plan.dto.response.Plan1ResponseDto;
import com.cluting.clutingbackend.plan.dto.response.Plan3ResponseDto;
import com.cluting.clutingbackend.plan.dto.response.Plan5ResponseDto;
import com.cluting.clutingbackend.plan.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "모집하기(1)~(5)",description = "모집하기 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {

    private PlanService planService;

    @PostMapping("/stage1/{recruitId}")
    @RequiredPermission(PermissionLevel.ONE)
    @Operation(summary = "모집하기(1)",description = "합격 인원 설정하기")
    public ResponseEntity<Plan1ResponseDto> stage1(@PathVariable(name="recruitId")Long recruitId, @RequestBody Plan1RequestDto dto){
            Plan1ResponseDto plan1ResponseDto = planService.createRecruitment(recruitId, dto);
        return ResponseEntity.ok(plan1ResponseDto);
    }

    @PostMapping("/stage2/{recruitId}")
    @RequiredPermission(PermissionLevel.TWO)
    @Operation(summary = "모집하기(2)",description = "인재상 구축하기")
    public ResponseEntity<Void> stage2(@PathVariable(name="recuritId") Long recruitId, @RequestBody Plan2RequestDto dto) {
        planService.saveTalentProfiles(recruitId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/stage3/{recruitId}")
    @RequiredPermission(PermissionLevel.THREE)
    @Operation(summary = "모집하기(3) post", description = "공고 작성하기")
    public ResponseEntity<Void> updateRecruitmentStage3(@PathVariable(name = "recruitId") Long recruitId, @RequestBody Plan3RequestDto requestDto) {
        planService.updateRecruitmentStage3(recruitId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/stage3/{recruitId}")
    @RequiredPermission(PermissionLevel.THREE)
    @Operation(summary = "모집하기(3) get", description = "공고 스케줄 알려주기")
    public ResponseEntity<Plan3ResponseDto> showSchedule(@PathVariable(name = "recruitId") Long recruitId) {
        Plan3ResponseDto dto = planService.showSchedule(recruitId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/stage4/{recruitId}/interview-setup")
    @RequiredPermission(PermissionLevel.FOUR)
    @Operation(summary = "모집하기(4)",description = "운영진 면접 일정 조정하기-면접세팅")
    public ResponseEntity<Void> setupInterview(@PathVariable(name="recruitId") Long recruitId, @RequestBody InterviewSetupDto requestDto) {
        planService.saveInterviewSetup(recruitId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/stage4/interview-time-slots")
    @Operation(summary = "모집하기(4)",description = "운영진 면접 일정 조정하기-면접가능시간 선택")
    public ResponseEntity<Void> saveInterviewTimeSlots(
            @RequestBody List<LocalDateTime> timeSlots,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        planService.saveTimeSlots(timeSlots, currentUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PostMapping("/stage5/{recruitId}")
    @RequiredPermission(PermissionLevel.FIVE)
    @Operation(summary = "모집하기(5)",description = "지원서 폼 제작하기")
    public ResponseEntity<Plan5ResponseDto> createApplicationForm(
            @PathVariable Long recruitId,
            @RequestBody Plan5RequestDto requestDto) {
        Plan5ResponseDto responseDto = planService.createApplicationForm(recruitId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

}
