package com.cluting.clutingbackend.plan.controller;

import com.cluting.clutingbackend.plan.domain.TalentProfile;
import com.cluting.clutingbackend.plan.dto.request.Plan1UpdateRequestDto;
import com.cluting.clutingbackend.plan.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/plan")
public class PlanController {

    @Autowired
    private PlanService planService;

    @PostMapping("/stage1/{postId}")
    public ResponseEntity<String> updatePartNums(@PathVariable Long postId,@RequestBody List<Plan1UpdateRequestDto> plan1UpdateRequestDtoList) {
        planService.updatePartNums(plan1UpdateRequestDtoList);
        return ResponseEntity.ok("Parts updated successfully");
    }

    // 파트에 인재상 추가
    @PostMapping("/stage2/{postId}")
    public ResponseEntity<String> addTalentProfilesToPart(
            @PathVariable Long postId, @RequestBody List<String> talentDescriptions) {
        planService.addTalentProfilesToPart(postId, talentDescriptions);
        return ResponseEntity.ok("Talent profiles added successfully to part ID: " + postId);
    }

    // 특정 파트의 인재상 조회
    @GetMapping("/{postId}/talent-profiles")
    public ResponseEntity<Map<String, List<String>>> getTalentProfilesByPost(@PathVariable Long postId) {
        Map<String, List<String>> profiles = planService.getTalentProfilesByPost(postId);
        return ResponseEntity.ok(profiles);
    }

}
