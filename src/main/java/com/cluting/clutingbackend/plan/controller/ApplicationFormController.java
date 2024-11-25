package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.dto.request.ApplicationFormRequestDto;
import com.cluting.clutingbackend.plan.dto.response.ApplicationFormResponseDto;
import com.cluting.clutingbackend.plan.service.ApplicationFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="모집하기(5) - 지원서 폼 작성하기")
@RestController
@RequestMapping("/api/application-forms")
@RequiredArgsConstructor
public class ApplicationFormController {

    private final ApplicationFormService applicationFormService;

    @Operation(summary="지원서 폼 만들기")
    @PostMapping("/stage5/create-form/{postId}")
    public ResponseEntity<ApplicationFormRequestDto> createApplicationForm(@PathVariable(name="postId") Long id,@RequestBody ApplicationFormRequestDto dto) {
        ApplicationFormRequestDto applicationFormRequestDto = applicationFormService.createApplicationForm(id,dto);
        return ResponseEntity.ok(applicationFormRequestDto);
    }

//    @Operation(summary = "지원서 폼 내용 가져오기")
//    @GetMapping("/stage5/{postId}")
//    public ResponseEntity<ApplicationFormResponseDto> getApplicationForm(@PathVariable(name="postId") Long id) {
//        ApplicationFormResponseDto responseDto = applicationFormService.getApplicationForm(id);
//        return ResponseEntity.ok(responseDto);
//    }

}

