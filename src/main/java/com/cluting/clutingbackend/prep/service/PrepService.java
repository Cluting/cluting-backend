package com.cluting.clutingbackend.prep.service;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Part;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.RecruitSchedule;
import com.cluting.clutingbackend.plan.repository.PartRepository;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import com.cluting.clutingbackend.prep.domain.PrepStageClubUser;
import com.cluting.clutingbackend.prep.domain.RecruitGroup;
import com.cluting.clutingbackend.prep.domain.PrepStage;
import com.cluting.clutingbackend.prep.dto.*;
import com.cluting.clutingbackend.prep.repository.GroupRepository;
import com.cluting.clutingbackend.prep.repository.PrepStageClubUserRepository;
import com.cluting.clutingbackend.prep.repository.PrepStageRepository;
import com.cluting.clutingbackend.plan.repository.UserRepository;
import com.cluting.clutingbackend.recruitingHome.repository.ClubUserRecruitingHomeRepository;
import com.cluting.clutingbackend.recruitingHome.repository.RecruitScheduleHomeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrepService {
    private final PostRepository postRepository;
    private final PrepStageRepository prepStageRepository;
    private final PartRepository partRepository;
    private final RecruitScheduleHomeRepository recruitScheduleHomeRepository;
    private final ClubUserRecruitingHomeRepository clubUserRecruitingHomeRepository;
    private final PrepStageClubUserRepository prepStageClubUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public PrepService(
            PostRepository postRepository,
            PrepStageRepository prepStageRepository,
            PartRepository partRepository,
            RecruitScheduleHomeRepository recruitScheduleHomeRepository,
            ClubUserRecruitingHomeRepository clubUserRecruitingHomeRepository,
            PrepStageClubUserRepository prepStageClubUserRepository,
            UserRepository userRepository) {
        this.prepStageRepository = prepStageRepository;
        this.partRepository = partRepository;
        this.recruitScheduleHomeRepository = recruitScheduleHomeRepository;
        this.clubUserRecruitingHomeRepository = clubUserRecruitingHomeRepository;
        this.postRepository = postRepository;
        this.prepStageClubUserRepository = prepStageClubUserRepository;
        this.userRepository = userRepository;
    }

    public List<RecruitSchedule> getRecruitSchedules(Long postId) {
        return recruitScheduleHomeRepository.findByPostId(postId);
    }

    public List<String> getPrepStages(Long postId) {
        return prepStageRepository.findByPostIdOrderByStageOrderAsc(postId)
                .stream()
                .map(PrepStage::getStageName)
                .collect(Collectors.toList());
    }

    public List<String> getClubUserNames(Long clubId) {
        return clubUserRecruitingHomeRepository.findByClubId(clubId)
                .stream()
                .map(clubUser -> clubUser.getUser().getName())
                .collect(Collectors.toList());
    }

    public List<String> getPartNames(Long postId) { // 메서드 이름 변경
        return partRepository.findByPostId(postId) // PartRepository 사용
                .stream()
                .map(Part::getName) // Part 엔티티의 name 필드로 변경
                .collect(Collectors.toList());
    }

    public List<PrepStageDto> getPrepStagesWithUserNames(Long postId) {
        List<PrepStage> prepStages = prepStageRepository.findByPostId(postId);
        return prepStages.stream()
                .map(PrepStageDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void createRecruitingPrep(Long clubId, Long postId, PrepRequestDto request) {
        // Step 1: Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Step 2: RecruitSchedule 저장
        RecruitSchedule recruitSchedule = new RecruitSchedule();
        recruitSchedule.setPost(post);
        BeanUtils.copyProperties(request.getRecruitSchedules(), recruitSchedule);
        recruitScheduleHomeRepository.save(recruitSchedule);

        // Step 3: PrepStage와 관련된 ClubUser 저장
        for (PrepStageDto prepStageDto : request.getPrepStages()) {
            PrepStage prepStage = new PrepStage();
            prepStage.setPost(post);
            prepStage.setStageName(prepStageDto.getStageName());
            prepStage.setStageOrder(prepStageDto.getStageOrder());

            // PrepStageClubUser 객체 리스트 생성
            List<PrepStageClubUser> prepStageClubUsers = new ArrayList<>();
            for (Long clubUserId : prepStageDto.getClubUserIds()) {
                // ClubUser 객체를 직접 조회
                ClubUser clubUser = clubUserRecruitingHomeRepository.findById(clubUserId)
                        .orElseThrow(() -> new IllegalArgumentException("ClubUser not found"));

                // PrepStageClubUser 생성 후 ClubUser 할당
                PrepStageClubUser prepStageClubUser = new PrepStageClubUser();
                prepStageClubUser.setPrepStage(prepStage);
                prepStageClubUser.setClubUser(clubUser);

                prepStageClubUsers.add(prepStageClubUser);  // 리스트에 추가
            }

            // PrepStageClubUser 리스트를 PrepStage에 할당
            prepStage.setPrepStageClubUsers(prepStageClubUsers);
            prepStageRepository.save(prepStage);  // PrepStage 저장

            // 각 PrepStageClubUser를 별도로 저장
            for (PrepStageClubUser prepStageClubUser : prepStageClubUsers) {
                prepStageClubUserRepository.save(prepStageClubUser);
            }
        }

        // Step 4: Part 저장
        for (PartDto partDto : request.getParts()) {
            Part part = new Part();
            part.setPost(post);
            part.setName(partDto.getName());
            partRepository.save(part);
        }
    }


}
