package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.clubuser.domain.ClubUserPermission;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.cluting.clutingbackend.global.exception.CustomException;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.plan.dto.request.*;
import com.cluting.clutingbackend.plan.dto.response.*;
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

import static com.cluting.clutingbackend.global.exception.ErrorCode.PERMISSION_DENIED;

@Tag(name = "모집하기(1)~(5)",description = "모집하기 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    private void checkPermission(CustomUserDetails currentUser, PermissionLevel requiredLevel) {
        // ClubUserPermission에서 PermissionLevel 필드만 추출
        boolean hasPermission = currentUser.getSelectedClubUser().getPermissionLevels().stream()
                .map(ClubUserPermission::getPermissionLevel) // PermissionLevel 추출
                .anyMatch(permission -> permission == requiredLevel); // 요구되는 권한과 비교

        // 권한이 없으면 예외 발생
        if (!hasPermission) {
            System.out.println("[현재 ClubUser] " + currentUser.getSelectedClubUser().toString());
            System.out.println("필요 권한: " + requiredLevel);
            throw new CustomException(
                    PERMISSION_DENIED,
                    "|  [모집하기 단계/Permission Denied] " + currentUser.getId() + "님의 접근 권한이 없습니다."
            );
        }
    }


    @PostMapping("/stage1/{recruitId}")
    @Operation(summary = "모집하기(1)",description = "합격 인원 설정하기")
    public ResponseEntity<Plan1ResponseDto> stage1(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable(name="recruitId")Long recruitId, @RequestBody Plan1RequestDto dto){
        checkPermission(currentUser, PermissionLevel.ONE);
        Plan1ResponseDto plan1ResponseDto = planService.createRecruitment(recruitId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(plan1ResponseDto);
    }

    @PostMapping("/stage2/{recruitId}")
    @Operation(summary = "모집하기(2)",description = "인재상 구축하기")
    public ResponseEntity<Void> stage2(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable(name="recruitId")Long recruitId, @RequestBody Plan2RequestDto dto) {
        checkPermission(currentUser, PermissionLevel.TWO);
        planService.saveIdeals(recruitId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/stage3/{recruitId}")
    @Operation(summary = "모집하기(3) POST 요청", description = "공고 작성하기")
    public ResponseEntity<Plan3RequestDto> updateRecruitmentStage3(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable(name = "recruitId") Long recruitId,
            @RequestBody Plan3RequestDto requestDto) {

        checkPermission(currentUser, PermissionLevel.THREE);
        planService.saveRecruitmentStage3(recruitId, requestDto);
        return ResponseEntity.ok(requestDto);
    }


    @PostMapping("/stage4/{recruitId}/interview-setup")
    @Operation(summary = "모집하기(4) POST 요청 | 면접 형식 세팅",description = "운영진 면접 일정 조정하기 - 면접세팅")
    public ResponseEntity<InterviewSetupDto> setupInterview(@AuthenticationPrincipal CustomUserDetails currentUser,
                                               @PathVariable(name="recruitId") Long recruitId,
                                               @RequestBody InterviewSetupDto requestDto) {
        checkPermission(currentUser, PermissionLevel.FOUR);
        planService.saveInterviewSetup(currentUser.getSelectedClubUser().getId(),recruitId, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(requestDto);
    }

    @PostMapping("/stage4/{recruitId}/possible-time-slots")
    @Operation(summary = "모집하기(4) POST 요청 | 면접 가능 시간 선택",description = "운영진 면접 일정 조정하기 - 면접 가능 시간 선택")
    public ResponseEntity<Void> savePossibleTimeSlots(
            @PathVariable(name="recruitId") Long recruitId,
            @RequestBody List<LocalDateTime> timeSlots,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        planService.saveTimeSlots(recruitId, timeSlots, currentUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/stage4/{recruitId}/assign-time-slots")
    @Operation(summary = "모집하기(4) POST 요청 | 면접관 일정 확정하기",description = "운영진 면접 일정 조정하기 - 면접관 일정 확정하기")
    public ResponseEntity<Void> saveAssignedTimeSlots(
            @RequestBody InterviewerAssignedDto interviewerAssignedDto,
            @PathVariable(name="recruitId") Long recruitId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        planService.assignTimeSlots(interviewerAssignedDto, currentUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/stage5/{recruitId}")
    @Operation(summary = "모집하기(5)",description = "지원서 폼 제작하기")
    public ResponseEntity<Plan5ResponseDto> createApplicationForm(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long recruitId,
            @RequestBody Plan5RequestDto requestDto) {
        checkPermission(currentUser, PermissionLevel.FIVE);
        Plan5ResponseDto responseDto = planService.createApplicationForm(recruitId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    ////////////////////////////////////////////GET/////////////////////////////////////////////////////////////////

    @GetMapping("/stage3/{recruitId}")
    @Operation(summary = "모집하기(3) GET 요청", description = "공고 정보 가져오기")
    public ResponseEntity<Plan3RequestDto> getRecruitmentStage3(@AuthenticationPrincipal CustomUserDetails currentUser,@PathVariable(name = "recruitId") Long recruitId) {
        Plan3RequestDto responseDto = planService.getRecruitmentStage3(recruitId);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/stage4/{recruitId}/interview-setup")
    @Operation(summary = "모집하기(4) GET 요청 | 면접 세팅 정보 전달",description = "운영진 면접 일정 조정하기 - 면접 세팅 정보 전달")
    public ResponseEntity<Plan4ResponseDto> getInterviewInfo(@PathVariable(name="recruitId") Long recruitId) {
        Plan4ResponseDto response = planService.getInterviewSetup(recruitId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stage4/{recruitId}/interviewer-time-slot")
    @Operation(summary = "모집하기(4) GET 요청 | 면접 시간 정보 전달",description = "운영진 면접 일정 조정하기 - 면접관 일정 정보")
    public ResponseEntity<InterviewTimeSlotResponseDto> getPossibleTimeSlots(@PathVariable(name="recruitId") Long recruitId) {
        InterviewTimeSlotResponseDto response = planService.getTimeSlots(recruitId);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/details/{recruitId}")
    @Operation(summary = "합격 인원 및 인재상 확인")
    public ResponseEntity<RecruitDetailResponseDto> getRecruitDetails(@PathVariable Long recruitId) {
        RecruitDetailResponseDto response = planService.getRecruitDetails(recruitId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/details-form/{recruitId}")
    @Operation(summary = "지원서 폼 확인")
    public ResponseEntity<Plan5ResponseDto> getApplicationFormDetails(@PathVariable Long recruitId) {
        Plan5ResponseDto response = planService.getFormDetail(recruitId);
        return ResponseEntity.ok(response);
    }
    ////////////////////////////////////////////PATCH/////////////////////////////////////////////////////////////////
    @PatchMapping("/stage1/{recruitId}")
    @Operation(summary = "모집하기(1) PATCH 요청", description = "합격 인원 부분 수정")
    public ResponseEntity<Plan1ResponseDto> patchStage1(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable(name = "recruitId") Long recruitId,
            @RequestBody Plan1RequestDto dto) {
        checkPermission(currentUser, PermissionLevel.ONE);
        Plan1ResponseDto responseDto = planService.updatePartialRecruit(recruitId, dto);
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/stage2/{recruitId}")
    @Operation(summary = "모집하기(2) PATCH 요청", description = "인재상 부분 수정")
    public ResponseEntity<Plan2RequestDto> patchStage2(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable(name = "recruitId") Long recruitId,
            @RequestBody Plan2RequestDto dto) {
        checkPermission(currentUser, PermissionLevel.TWO);
        planService.updatePartialIdeals(recruitId, dto);
        return ResponseEntity.ok(dto);
    }


    @PatchMapping("/stage3/{recruitId}")
    @Operation(summary = "모집하기(3) PATCH 요청", description = "공고 일부 수정")
    public ResponseEntity<Plan3RequestDto> patchRecruitmentStage3(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable(name = "recruitId") Long recruitId,
            @RequestBody Plan3RequestDto requestDto) {

        checkPermission(currentUser, PermissionLevel.THREE);
        planService.updatePartialRecruitmentStage3(recruitId, requestDto);
        return ResponseEntity.ok(requestDto);
    }

//    @PatchMapping("/stage4/{recruitId}/interview-setup")
//    @Operation(summary = "모집하기(4) PATCH 요청 | 면접 세팅 부분 수정", description = "운영진 면접 일정 조정하기 - 부분 수정")
//    public ResponseEntity<Void> patchInterviewSetup(
//            @AuthenticationPrincipal CustomUserDetails currentUser,
//            @PathVariable(name = "recruitId") Long recruitId,
//            @RequestBody InterviewSetupDto dto) {
//        checkPermission(currentUser, PermissionLevel.FOUR);
//        planService.updatePartialInterviewSetup(recruitId, dto);
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }

    @PatchMapping("/stage5/{recruitId}")
    @Operation(summary = "모집하기(5) PATCH 요청", description = "지원서 폼 일부 수정")
    public ResponseEntity<Plan5ResponseDto> patchApplicationForm(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long recruitId,
            @RequestBody Plan5RequestDto requestDto) {
        checkPermission(currentUser, PermissionLevel.FIVE);
        Plan5ResponseDto responseDto = planService.updatePartialApplicationForm(recruitId, requestDto);
        return ResponseEntity.ok(responseDto);
    }


}
