package com.cluting.clutingbackend.evaluate.service;

import com.cluting.clutingbackend.evaluate.dto.response.ClubUserResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.ClubUsersResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.DocEvaluatePrepResponseDto;
import com.cluting.clutingbackend.evaluate.dto.response.PartResponseDto;
import com.cluting.clutingbackend.evaluate.repository.EvaluationRepository;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.repository.ClubUserRepository;
import com.cluting.clutingbackend.part.PartRepository;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluateService {
    private final PostRepository postRepository;
    private final EvaluationRepository evaluationRepository;
    private final PartRepository partRepository;
    private final ClubUserRepository clubUserRepository;

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
}
