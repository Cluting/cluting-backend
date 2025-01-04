package com.cluting.clutingbackend.global.security;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import com.cluting.clutingbackend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {


    @Before("@annotation(requiredPermission)")
    public void checkPermissionLevel(RequiredPermission requiredPermission) {
        // 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // CLubUser 조회
        ClubUser clubUser = customUserDetails.getClubUser();

        List<PermissionLevel> userPermissionLevels = clubUser.getPermissionLevels();
        System.out.println("ClubUser의 권한 레벨 체크" + userPermissionLevels);

        if (userPermissionLevels == null || userPermissionLevels.isEmpty()) {
            throw new SecurityException("사용자의 권한 정보가 없습니다.");
        }

        // 요구되는 권한 레벨
        PermissionLevel requiredLevel = requiredPermission.value();
        System.out.println("요구되는 권한 레벨 " +  requiredLevel);

        // 권한 리스트에서 요구되는 레벨이 포함되어 있는지 확인
        if (!userPermissionLevels.contains(requiredLevel)) {
            throw new SecurityException("사용자의 권한이 부족합니다.");
        }
    }
}

