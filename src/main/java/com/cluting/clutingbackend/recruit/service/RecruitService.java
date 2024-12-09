package com.cluting.clutingbackend.recruit.service;


import com.cluting.clutingbackend.application.domain.Application;
import com.cluting.clutingbackend.application.repository.ApplicationRepository;
import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.enums.Category;
import com.cluting.clutingbackend.global.enums.ClubType;
import com.cluting.clutingbackend.global.enums.SortType;
import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.global.util.StaticValue;
import com.cluting.clutingbackend.plan.domain.DocumentEvaluator;
import com.cluting.clutingbackend.plan.domain.Group;
import com.cluting.clutingbackend.plan.repository.DocumentCriteriaRepository;
import com.cluting.clutingbackend.plan.repository.DocumentEvaluatorRepository;
import com.cluting.clutingbackend.plan.repository.GroupRepository;
import com.cluting.clutingbackend.recruit.domain.Recruit;
import com.cluting.clutingbackend.recruit.dto.request.RecruitCriteriaSaveRequestDto;
import com.cluting.clutingbackend.recruit.dto.request.RecruitDocSetRequestDto;
import com.cluting.clutingbackend.recruit.dto.request.RecruitRoleAllocateRequestDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitDocPrepSavedResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitNumResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitResponseDto;
import com.cluting.clutingbackend.recruit.dto.response.RecruitsResponseDto;
import com.cluting.clutingbackend.recruit.repository.RecruitRepository;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final GroupRepository groupRepository;
    private final ClubUserRepository clubUserRepository;
    private final ApplicationRepository applicationRepository;
    private final DocumentCriteriaRepository documentCriteriaRepository;
    private final DocumentEvaluatorRepository documentEvaluatorRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public RecruitsResponseDto findAll(Integer pageNum, SortType sortType, ClubType clubType, Category category) {
        int pageSize = StaticValue.PAGE_DEFAULT_SIZE;
        int skipCount = (pageNum - 1) * pageSize;

        List<RecruitResponseDto> recruitDtos = recruitRepository.findAll().stream()
                .map(RecruitResponseDto::toDto)
                .filter(dto -> clubType == null || dto.getClubType() == clubType)
                .filter(dto -> category == null || dto.getCategory() == category)
                .sorted((dto1, dto2) -> {
                    if (sortType == null) return 0;
                    return switch (sortType) {
                        case DEADLINE -> dto1.getDeadLine().compareTo(dto2.getDeadLine());
                        case NEWEST -> dto2.getCreatedAt().compareTo(dto1.getCreatedAt());
                        case OLDEST -> dto1.getCreatedAt().compareTo(dto2.getCreatedAt());
                        default -> 0;
                    };
                })
                .skip(skipCount)
                .limit(pageSize)
                .toList();

        return new RecruitsResponseDto(recruitDtos.size(), recruitDtos);
    }

    @Transactional(readOnly = true)
    public RecruitResponseDto findById(Long recruitId) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 리크루팅 입니다."
                        )
                );

        return RecruitResponseDto.toDto(recruit);
    }

    @Transactional(readOnly = true)
    public RecruitNumResponseDto findAppliedNum(Long recruitId) {
        Map<String, Integer> groupMap = new HashMap<>();
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        int totalNum = 0;
        for (Group group : groups) {
            totalNum += group.getNumRecruit();
            String groupName = group.getName();
            if (groupName != null) {
                groupMap.put(groupName, group.getNumRecruit());
            }
        }

        return new RecruitNumResponseDto(totalNum, groupMap);
    }

    @Transactional(readOnly = true)
    public RecruitNumResponseDto findDocPassNum(Long recruitId) {
        Map<String, Integer> groupMap = new HashMap<>();
        List<Group> groups = groupRepository.findByRecruitId(recruitId);
        int totalNum = 0;
        for (Group group : groups) {
            totalNum += group.getNumRecruit();
            String groupName = group.getName();
            if (groupName != null) {
                groupMap.put(groupName, group.getNumDoc());
            }
        }

        return new RecruitNumResponseDto(totalNum, groupMap);
    }

    @Transactional
    public RecruitDocPrepSavedResponseDto saveDocRecruit(Long recruitId, RecruitDocSetRequestDto recruitDocSetRequestDto) {
        Recruit recruit = recruitRepository.findById(recruitId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 리크루팅 입니다."
                        )
                );
        List<Group> groups                       = groupRepository.findByRecruitId(recruitId);
        List<Application> applications           = applicationRepository.findByRecruitId(recruitId);
        Map<String, Group> groupMap                = new HashMap<>();
        Map<String, Application> applicationMap  = new HashMap<>();
        for (Group group : groups) groupMap.put(group.getName(), group);
        for (Application application : applications) applicationMap.put(application.getRecruit_group(), application);

        // 공통일 경우에는 그룹 추가 (그룹이 추가된 경우)
        // 모집공고에 대한 그룹이 한 개이며, (그룹 이름이 null 이거나 공통이고), requestDto 에 입력된 그룹의 개수가 1개 초과일 경우(= 그룹 추가가 이뤄진 경우)
        Group firstGroup = groups.get(0);
        if (groups.size() == 1 && (firstGroup.getName() == null || firstGroup.getName().equals("공통")) && recruitDocSetRequestDto.getGroups().size() > 1) {
            int groupCount = recruitDocSetRequestDto.getGroups().size();
            int numRecruit = firstGroup.getNumRecruit();
            int divNum = numRecruit / groupCount;
            int modNum = numRecruit % groupCount;
            for (int i = 0; i < groupCount; i++) {
                if (i == 0) {
                    firstGroup.setName(recruitDocSetRequestDto.getGroups().get(i).getGroupName());
                    firstGroup.setNumRecruit(divNum);
                    if(modNum > 0) firstGroup.setNumRecruit(divNum + 1);
                    modNum--;
                    groupRepository.save(firstGroup);
                }
                int newRecruitNum = divNum;
                if(modNum > 0) {
                    newRecruitNum++;
                    modNum--;
                }
                String newGroupName = recruitDocSetRequestDto.getGroups().get(i).getGroupName();
                groupRepository.save(Group.of(firstGroup.getRecruit(), newGroupName, firstGroup.getNumDoc(), firstGroup.getNumFinal(), newRecruitNum, firstGroup.getWarning()));
            }

            // 새로운 데이터셋
            groups = groupRepository.findByRecruitId(recruitId);
            groupMap.clear();
            for (Group group : groups) groupMap.put(group.getName(), group);
        }

        // 서류 평가 기준 저장
        for (RecruitRoleAllocateRequestDto dto : recruitDocSetRequestDto.getGroups()) {
            dto.getCriteria().forEach(documentCriteriaRepository.save(RecruitCriteriaSaveRequestDto::toEntity));
        }

        // 서류 평가 중간 테이블 저장
        for (RecruitRoleAllocateRequestDto dto : recruitDocSetRequestDto.getGroups()) {
            Group group = groupMap.get(dto.getGroupName());
            if (group == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 그룹입니다.");

            int numRecruit = group.getNumRecruit();
            int count = 0;

            // 그룹에 할당된 ClubUser(admins 리스트)
            List<Long> adminIds = dto.getAdmins();
            List<ClubUser> clubUsers = new ArrayList<>();
            for (Long adminId : adminIds) {
                ClubUser clubUser = getClubUser(adminId);
                clubUsers.add(clubUser);
            }

            // 각 Application에 대해 DocumentEvaluator 생성
            for (Application application : applications) {
                if (count >= numRecruit) break;
                ClubUser assignedClubUser = clubUsers.get(count % clubUsers.size());
                DocumentEvaluator evaluator = DocumentEvaluator.of(
                        assignedClubUser,
                        application,
                        group,
                        Stage.BEFORE,
                        null,
                        null
                );
                documentEvaluatorRepository.save(evaluator);
                count++;
            }
        }

        return RecruitDocPrepSavedResponseDto.builder().build(); //TODO
    }

    public ClubUser getClubUser(Long clubUserId) {
        return clubUserRepository.findById(clubUserId)
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "존재하지 않는 운영진 입니다."
                        )
                );

    }
}
