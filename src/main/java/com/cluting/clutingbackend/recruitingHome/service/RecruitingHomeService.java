package com.cluting.clutingbackend.recruitingHome.service;

import com.cluting.clutingbackend.plan.domain.*;
import com.cluting.clutingbackend.recruitingHome.repository.ClubRecruitingHomeRepository;
import com.cluting.clutingbackend.recruitingHome.repository.ClubUserRecruitingHomeRepository;
import com.cluting.clutingbackend.recruitingHome.repository.PostRecruitingHomeRepository;
import com.cluting.clutingbackend.recruitingHome.repository.RecruitScheduleHomeRepository;
import com.cluting.clutingbackend.recruitingHome.repository.TodoRecruitingHomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruitingHomeService {
    private final ClubRecruitingHomeRepository clubRecruitingHomeRepository;
    private final ClubUserRecruitingHomeRepository clubUserRecruitingHomeRepository;
    private final PostRecruitingHomeRepository postRecruitingHomeRepository;
    private final RecruitScheduleHomeRepository recruitScheduleHomeRepository;
    private final TodoRecruitingHomeRepository todoRecruitingHomeRepository;

    @Autowired
    public RecruitingHomeService(ClubRecruitingHomeRepository clubRecruitingHomeRepository,
                                 ClubUserRecruitingHomeRepository clubUserRecruitingHomeRepository,
                                 PostRecruitingHomeRepository postRecruitingHomeRepository,
                                 RecruitScheduleHomeRepository recruitScheduleHomeRepository,
                                 TodoRecruitingHomeRepository todoRecruitingHomeRepository) {
        this.clubRecruitingHomeRepository = clubRecruitingHomeRepository;
        this.clubUserRecruitingHomeRepository = clubUserRecruitingHomeRepository;
        this.postRecruitingHomeRepository = postRecruitingHomeRepository;
        this.recruitScheduleHomeRepository = recruitScheduleHomeRepository;
        this.todoRecruitingHomeRepository = todoRecruitingHomeRepository;
    }

    // 1. Club 데이터를 clubId로 찾기
    public Optional<Club> getClubById(Long clubId) {
        return clubRecruitingHomeRepository.findById(clubId);
    }

    // 2. Post 데이터를 clubId로 찾기 (currentStage, isInterview, generation)
    public Optional<Post> getPostByClubId(Long clubId, Long postId) {
        return postRecruitingHomeRepository.findByClubIdAndId(clubId, postId);
    }

    // 3. RecruitSchedule 데이터를 postId로 찾기
    public List<RecruitSchedule> getRecruitSchedulesByPostId(Long postId) {
        return recruitScheduleHomeRepository.findByPostId(postId);
    }

    // 4. ClubUser 데이터를 clubId로 찾기 (모든 user 리스트)
    public List<ClubUser> getClubUsersByClubId(Long clubId) {
        return clubUserRecruitingHomeRepository.findByClubId(clubId);
    }

    // 5. Todo 데이터를 userId로 찾기 (Authorization 토큰에 포함된 userId 사용)
    public List<Todo> getTodosByUserId(Long userId) {
        return todoRecruitingHomeRepository.findByUserId(userId);
    }
}
