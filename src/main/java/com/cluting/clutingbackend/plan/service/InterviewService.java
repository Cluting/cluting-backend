package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.ClubUser;
import com.cluting.clutingbackend.plan.domain.Interview;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.TimeSlot;
import com.cluting.clutingbackend.plan.dto.request.AssignTimeSlotDto;
import com.cluting.clutingbackend.plan.dto.request.InterviewAssignmentDto;
import com.cluting.clutingbackend.plan.dto.request.InterviewSetupDto;
import com.cluting.clutingbackend.plan.dto.request.TimeSlotAvailabilityDto;
import com.cluting.clutingbackend.plan.dto.response.AvailableTimeSlotDto;
import com.cluting.clutingbackend.plan.repository.ClubUserRepository;
import com.cluting.clutingbackend.plan.repository.InterviewRepository;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import com.cluting.clutingbackend.plan.repository.TimeSlotRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService  {
    private final InterviewRepository interviewRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final PostRepository postRepository;
    private final ClubUserRepository clubUserRepository;

    public Post setUpInterview(InterviewSetupDto dto) {
        Optional<Post> optionalPost = postRepository.findById(dto.getPostId());

        Post post = optionalPost.orElseThrow(() -> new RuntimeException("Post is not found!"));

        post.setInterviewerCount(dto.getInterviewerCount());
        post.setIntervieweeCount(dto.getIntervieweeCount());
        post.setInterviewDuration(dto.getInterviewDuration());

        return postRepository.save(post);
    }


    public void saveSelectedSlots(TimeSlotAvailabilityDto dto) {
        ClubUser clubUser = clubUserRepository.findById(dto.getClubUserId())
                .orElseThrow(() -> new RuntimeException("ClubUser not found!"));

        List<TimeSlotAvailabilityDto.TimeSlotDto> selectedSlots = dto.getTimeSlots();

        selectedSlots.forEach(slot -> {
            TimeSlot timeSlot = TimeSlot.builder()
                    .times(LocalDateTime.of(slot.getDate(), slot.getTime()))
                    .isAvailable(slot.isAvailable())
                    .clubUser(clubUser)
                    .build();

            timeSlotRepository.save(timeSlot);
        });
    }

    public List<AvailableTimeSlotDto> getAvailableInterviewers(Long clubUserId) {
        // 1. 인터뷰 ID로 관련 TimeSlot 가져오기
        List<TimeSlot> timeSlots = timeSlotRepository.findByClubUserId(clubUserId);

        // 2. 날짜 및 시간별 가능한 운영진을 저장할 맵 초기화
        Map<LocalDateTime, List<Long>> availabilityMap = new HashMap<>();

        // 3. TimeSlot을 순회하며 맵에 운영진 추가
        timeSlots.forEach(slot -> {
            if (slot.isAvailable()) { // 운영진이 해당 시간대에 가능할 경우만 처리
                LocalDateTime slotDateTime = slot.getTimes();
                availabilityMap.computeIfAbsent(slotDateTime, k -> new ArrayList<>()).add(slot.getClubUser().getId());
            }
        });

        // 4. Map을 List<AvailableTimeSlotDto>로 변환
        List<AvailableTimeSlotDto> result = new ArrayList<>();
        availabilityMap.forEach((timeSlot, clubUserIds) -> {
            result.add(new AvailableTimeSlotDto(timeSlot, clubUserIds));
        });

        return result;
    }



    public void assignInterviewers(Long postId, AssignTimeSlotDto dto) {
        // 인터뷰 ID에 해당하는 TimeSlot 가져오기
        List<TimeSlot> timeSlots = timeSlotRepository.findByClubUserId(postId);

        // DTO에서 전달된 시간대와 면접관 ID 매핑
        dto.getAssignedInterviewers().forEach((timeSlot, clubUserIds) -> {
            // 해당 시간대의 TimeSlot 필터링
            List<TimeSlot> matchingTimeSlots = timeSlots.stream()
                    .filter(slot -> slot.getTimes().equals(timeSlot))
                    .collect(Collectors.toList());

            if (matchingTimeSlots.isEmpty()) {
                throw new RuntimeException("No matching TimeSlot found for: " + timeSlot);
            }

            // 각 TimeSlot에 면접관 할당
            matchingTimeSlots.forEach(slot -> {
                clubUserIds.forEach(clubUserId -> {
                    ClubUser clubUser = clubUserRepository.findById(clubUserId)
                            .orElseThrow(() -> new RuntimeException("ClubUser not found: " + clubUserId));
                    slot.setClubUser(clubUser); // 면접관 할당
                    timeSlotRepository.save(slot); // 저장
                });
            });
        });
    }
}
