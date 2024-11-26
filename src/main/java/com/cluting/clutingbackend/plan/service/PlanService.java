package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.part.Part;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.TalentProfile;
import com.cluting.clutingbackend.plan.dto.request.PlanUpdateRequestDto;
import com.cluting.clutingbackend.plan.dto.request.PostRequestDto;
import com.cluting.clutingbackend.plan.repository.ClubRepository;
import com.cluting.clutingbackend.part.PartRepository;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import com.cluting.clutingbackend.plan.repository.TalentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private PostRepository postRepository;

    public void updatePartNums(List<PlanUpdateRequestDto> planUpdateRequestDtoList) {
        for (PlanUpdateRequestDto dto : planUpdateRequestDtoList) {
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
        List<Part> parts = partRepository.findByPostId(postId);
        Map<String, List<String>> result = new HashMap<>();

        for (Part part : parts) {
            List<String> profileDescriptions = part.getTalentProfiles().stream()
                    .map(TalentProfile::getDescription)
                    .collect(Collectors.toList());
            result.put(part.getName(), profileDescriptions);
        }

        return result;
    }

    public Post updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Post ID"));

        // Update only the modifiable fields
        post.setImageUrl(postRequestDto.getImageUrl());
        post.setTitle(postRequestDto.getTitle());
        post.setRecruitmentStartDate(postRequestDto.getRecruitmentStartDate());
        post.setRecruitmentEndDate(postRequestDto.getRecruitmentEndDate());
        post.setDocumentResultDate(postRequestDto.getDocumentResultDate());
        post.setFinalResultDate(postRequestDto.getFinalResultDate());
        post.setRecruitmentNumber(postRequestDto.getRecruitmentNumber());
        post.setActivityStart(postRequestDto.getActivityStart());
        post.setActivityEnd(postRequestDto.getActivityEnd());
        post.setActivitySchedule(postRequestDto.getActivitySchedule());
        post.setClubFee(postRequestDto.getClubFee());
        post.setContent(postRequestDto.getContent());

        return postRepository.save(post);
    }


}
