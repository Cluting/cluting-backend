package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.club.domain.Club;
import com.cluting.clutingbackend.club.repository.ClubRepository;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.dto.request.Plan1RequestDto;
import com.cluting.clutingbackend.plan.dto.response.Plan1ResponseDto;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final GroupRepository groupRepository;
    private final ClubRepository clubRepository;
    private final RecruitRepository recruitRepository;

    @Transactional
    public Plan1ResponseDto createRecruitment(Long clubId, Plan1RequestDto requestDto) {
        // Club 엔티티 조회
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new IllegalArgumentException("Club not found"));

        // Recruit 엔티티 생성 및 저장
        Recruit recruit = Recruit.builder()
                .club(club)
                .title("Default Title") // 필요에 따라 값 설정
                .numDoc(requestDto.getTotalDocumentPassCount())
                .numFinal(requestDto.getTotalFinalPassCount())
                .isDone(false) // 초기값 설정
                .build();
       recruitRepository.save(recruit);

        // 파트 정보가 있다면 Group 엔티티 생성 및 저장
        if (requestDto.getGroupInfos() != null && !requestDto.getGroupInfos().isEmpty()) {
            requestDto.getGroupInfos().forEach(groupInfo -> {
                Group group = Group.builder()
                        .recruit(recruit)
                        .name(groupInfo.getGroupName())
                        .numDoc(groupInfo.getDocumentPassCount())
                        .numFinal(groupInfo.getFinalPassCount())
                        .build();
                groupRepository.save(group);
            });
        }
        return Plan1ResponseDto.builder()
                .recruitId(recruit.getId())
                .title(recruit.getTitle())
                .description(recruit.getDescription())
                .totalDocumentPassCount(recruit.getNumDoc())
                .totalFinalPassCount(recruit.getNumFinal())
                .parts(recruit.getGroupList() != null ? recruit.getGroupList().stream().map(group ->
                        Plan1ResponseDto.PartInfo.builder()
                                .partName(group.getName())
                                .documentPassCount(group.getNumDoc())
                                .finalPassCount(group.getNumFinal())
                                .build()
                ).toList() : null)
                .build();
    }



}
