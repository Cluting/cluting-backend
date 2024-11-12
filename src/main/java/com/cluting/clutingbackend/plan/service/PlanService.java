package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.Part;
import com.cluting.clutingbackend.plan.domain.TalentProfile;
import com.cluting.clutingbackend.plan.dto.request.Plan1UpdateRequestDto;
import com.cluting.clutingbackend.plan.repository.PartRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlanService {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private TalentProfileRepository talentProfileRepository;

    public void updatePartNums(List<Plan1UpdateRequestDto> plan1UpdateRequestDtoList) {
        for (Plan1UpdateRequestDto dto : plan1UpdateRequestDtoList) {
            Part part = partRepository.findById(dto.getPartId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Part ID: " + dto.getPartId()));

            // Check if the part name is "공통"
            if ("공통".equals(part.getName())) {
                System.out.println("Updating '공통' part");
            }

            part.setNumDoc(dto.getNumDoc());
            part.setNumFinal(dto.getNumFinal());
            partRepository.save(part);
        }
    }

    public void addTalentProfilesToPart(Long postId, List<String> talentDescriptions) {
        Part part = partRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID: " + postId));

        for (String description : talentDescriptions) {
            TalentProfile profile = new TalentProfile();
            profile.setDescription(description);
            profile.setPart(part);
            talentProfileRepository.save(profile);
        }
    }

    // postId로 모든 파트의 인재상 조회 및 포맷팅
    public Map<String, List<String>> getTalentProfilesByPost(Long postId) {
        List<Part> parts = partRepository.findByPost_PostId(postId);
        Map<String, List<String>> result = new HashMap<>();

        for (Part part : parts) {
            List<String> profileDescriptions = part.getTalentProfiles().stream()
                    .map(TalentProfile::getDescription)
                    .collect(Collectors.toList());
            result.put(part.getName(), profileDescriptions);
        }

        return result;
    }



}
