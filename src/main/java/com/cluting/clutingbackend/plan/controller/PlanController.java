package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "모집하기(1)~(5)",description = "모집하기 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/plan")
@RequiredArgsConstructor
public class PlanController {

    private PlanService planService;

    @PostMapping("/stage1/{postId}")
    @Operation(summary = "모집하기(1)")
    public ResponseEntity<String> stage1(@PathVariable(name="postId")Long id){


        return ResponseEntity.ok("Parts Updated Successfully!");
    }

}
