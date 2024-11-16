package com.cluting.clutingbackend.prep.controller;

import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import com.cluting.clutingbackend.prep.dto.PrepRequestDto;
import com.cluting.clutingbackend.prep.dto.PrepStageDto;
import com.cluting.clutingbackend.prep.dto.RecruitScheduleDto;
import com.cluting.clutingbackend.prep.service.PrepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recruiting/prep")
public class PrepController {
    private final PrepService prepService;

    @Autowired
    public PrepController(PrepService prepService) {
        this.prepService = prepService;
    }

    @GetMapping
    public ResponseEntity<?> getRecruitingPrepData(
            @RequestParam Long clubId,
            @RequestParam Long postId) {

        List<RecruitSchedule> recruitSchedules = prepService.getRecruitSchedules(postId);
        List<RecruitScheduleDto> recruitScheduleDtos = recruitSchedules.stream()
                .map(RecruitScheduleDto::new)
                .collect(Collectors.toList());

        List<PrepStageDto> prepStagesWithUserNames = prepService.getPrepStagesWithUserNames(postId);
        List<String> clubUserNames = prepService.getClubUserNames(clubId);
        List<String> groupNames = prepService.getGroupNames(postId);

        Map<String, Object> response = new HashMap<>();
        response.put("recruitSchedules", recruitScheduleDtos);
        response.put("prepStages", prepStagesWithUserNames);
        response.put("clubUserNames", clubUserNames);
        response.put("groupNames", groupNames);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createRecruitingPrep(@RequestParam Long clubId,
                                                       @RequestParam Long postId,
                                                       @RequestBody PrepRequestDto request) {
        try {
            prepService.createRecruitingPrep(clubId, postId, request);
            return ResponseEntity.ok("Recruiting preparation data saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving recruiting preparation data.");
        }
    }
}
