package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
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

    @PostMapping("/stage1/{recruitId}")
    @RequiredPermission(PermissionLevel.ONE)
    @Operation(summary = "모집하기(1)",description = "합격 인원 설정하기")
    public ResponseEntity<String> stage1(@PathVariable(name="recruitId")Long id){

        return ResponseEntity.ok("Parts Updated Successfully!");
    }

    @PostMapping("/stage2/{postId}")
    @RequiredPermission(PermissionLevel.TWO)
    @Operation(summary = "모집하기(2)")
    public ResponseEntity<String> stage2(@PathVariable(name="postId")Long id){


        return ResponseEntity.ok("Parts Updated Successfully!");
    }

    @PostMapping("/stage3/{postId}")
    @RequiredPermission(PermissionLevel.THREE)
    @Operation(summary = "모집하기(3)")
    public ResponseEntity<String> stage3(@PathVariable(name="postId")Long id){


        return ResponseEntity.ok("Parts Updated Successfully!");
    }

    @PostMapping("/stage4/{postId}")
    @RequiredPermission(PermissionLevel.FOUR)
    @Operation(summary = "모집하기(4)")
    public ResponseEntity<String> stage4(@PathVariable(name="postId")Long id){


        return ResponseEntity.ok("Parts Updated Successfully!");
    }

    @PostMapping("/stage5/{postId}")
    @RequiredPermission(PermissionLevel.FIVE)
    @Operation(summary = "모집하기(5)")
    public ResponseEntity<String> stage5(@PathVariable(name="postId")Long id){


        return ResponseEntity.ok("Parts Updated Successfully!");
    }

}
