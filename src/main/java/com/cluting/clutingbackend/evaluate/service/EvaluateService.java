package com.cluting.clutingbackend.evaluate.service;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import com.cluting.clutingbackend.evaluate.dto.request.EvaluateCriteriaRequestDto;
import com.cluting.clutingbackend.evaluate.dto.request.EvaluatePrepRequestDto;
import com.cluting.clutingbackend.evaluate.dto.request.GroupStaffAllocateRequestDto;
import com.cluting.clutingbackend.evaluate.dto.response.*;
import com.cluting.clutingbackend.evaluate.repository.CriteriaRepository;
import com.cluting.clutingbackend.evaluate.repository.EvaluationRepository;
import com.cluting.clutingbackend.evaluate.repository.EvaluatorRepository;
import com.cluting.clutingbackend.part.Part;
import com.cluting.clutingbackend.part.PartController;
import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.domain.User;
import com.cluting.clutingbackend.plan.repository.ApplicationRepository;
import com.cluting.clutingbackend.plan.repository.ClubUserRepository;
import com.cluting.clutingbackend.part.PartRepository;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EvaluateService {
    private final PostRepository postRepository;
    private final EvaluationRepository evaluationRepository;
    private final PartRepository partRepository;
    private final ClubUserRepository clubUserRepository;
    private final ApplicationRepository applicationRepository;
    private final CriteriaRepository criteriaRepository;
    private final EvaluatorRepository evaluatorRepository;

    // 서류 평가하기 준비 (파트별 지원자 수, 파트명 조회, 설정한 서류 합격자 수 조회)
    @Transactional(readOnly = true)
    public DocEvaluatePrepResponseDto docEvaluatePrep(Long postId) {
        List<PartResponseDto> parts = partRepository.findByPostId(postId).stream().map(PartResponseDto::toDto).toList();
        int totalNum = 0;
        int totalDocNum = 0;
        for (PartResponseDto part : parts) {
            totalNum += part.getNumRecruit();
            totalDocNum += part.getNumDoc();
        }
        return DocEvaluatePrepResponseDto.toDto(totalNum, totalDocNum, parts);
    }

    // 해당 동아리의 운영진 리스트 조회 (담당자 선택에 필요)
    @Transactional(readOnly = true)
    public ClubUsersResponseDto clubStaffList(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "존재하지 않는 모집공고 입니다."
                )
        );

        List<ClubUserResponseDto> clubUsers = clubUserRepository.clubStaff(post.getClub().getId()).stream().map(ClubUserResponseDto::toDto).toList();
        return ClubUsersResponseDto.toDto(clubUsers);
    }

    // 서류 평가하기 저장
    @Transactional
    public DocPrepareResponseDto prepareDocEvaluate(Long postId, EvaluatePrepRequestDto evaluatePrepRequestDto) {
        List<Part> parts = partRepository.findByPostId(postId).stream().toList();

        Map<String, Part> partList = new HashMap<>();
        for (Part part : parts) partList.put(part.getName(), part);

        List<CriteriaCreateResponseDto> criteria = new ArrayList<>();
        for (EvaluateCriteriaRequestDto dto : evaluatePrepRequestDto.getCriteria()) {
            criteria.add(CriteriaCreateResponseDto.toDto(criteriaRepository.save(dto.toEntity(partList.get(dto.getGroupName())))));
        }

        List<EvaluateStaffAllocateResponseDto> staffs = new ArrayList<>();
        for (GroupStaffAllocateRequestDto dto : evaluatePrepRequestDto.getGroups()) {
            staffs.add(EvaluateStaffAllocateResponseDto.toDto(evaluatorRepository.save(dto.toEntity(partList.get(dto.getGroupName())))));
        }

        return DocPrepareResponseDto.toDto(staffs, criteria);
    }

    // part 목록 조회
    @Transactional(readOnly = true)
    public List<String> partList(Long applicationId) {
        return partRepository.findByPost_Id(applicationId).stream().map(Part::getName).toList();
    }

    // 서류 합격자/불합격자 조회
    @Transactional(readOnly = true)
    public DocAcceptedOrRejectedResponseDto acceptedOrRejected(User user, Long applicationId, Boolean recent, String part) {
        List<UserPartResponseDto> accepted = applicationRepository.findByState(applicationId, Application.State.DOCPASS)
                .stream()
                .map(application -> {
                    UserPartResponseDto dto = UserPartResponseDto.toDto(application);
                    dto.setRanking(application.getScore());
                    return dto;
                })
                .sorted((a, b) -> Integer.compare(b.getRanking(), a.getRanking()))
                .toList();
        List<UserPartResponseDto> rejected = applicationRepository.findByState(applicationId, Application.State.DOCFAIL)
                .stream()
                .map(application -> {
                    UserPartResponseDto dto = UserPartResponseDto.toDto(application);
                    dto.setRanking(application.getScore());
                    return dto;
                })
                .sorted((a, b) -> Integer.compare(b.getRanking(), a.getRanking()))
                .toList();

        // 최신순 정렬
        if (recent != null && recent) {
            accepted = accepted.stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList();
            rejected = rejected.stream()
                    .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                    .toList();
        }

        // 특정 part만 필터링
        if (part != null && !part.isEmpty()) {
            accepted = accepted.stream()
                    .filter(dto -> part.equals(dto.getPart()))
                    .toList();
            rejected = rejected.stream()
                    .filter(dto -> part.equals(dto.getPart()))
                    .toList();
        }

        Map<String, Integer> partNum = new HashMap<>();
        for (UserPartResponseDto a : accepted) partNum.put(a.getPart(), partNum.getOrDefault(a.getPart(), 0) + 1);

        return DocAcceptedOrRejectedResponseDto.toDto(partNum, accepted, rejected);
    }
}
