package com.cluting.clutingbackend.global.security;

import com.cluting.clutingbackend.global.annotation.RequiredPermission;
import com.cluting.clutingbackend.global.enums.PermissionLevel;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {
    @Before("@annotation(requiredPermission)")
    public void checkPermissionLevel(RequiredPermission requiredPermission){
        // 로그인한 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 권한 레벨 체크하기
        PermissionLevel userPermissionLevel = (PermissionLevel) authentication.getAuthorities()
                .stream()
                .map(grantedAuthority -> PermissionLevel.valueOf(grantedAuthority.getAuthority()))
                .findFirst()
                .orElseThrow(()-> new SecurityException("사용자의 권한 정보가 없습니다."));

        // 요구되는 권한 레벨
        PermissionLevel requiredLevel = requiredPermission.value();

        // 권한 비교
        if(userPermissionLevel.ordinal() != requiredLevel.ordinal()){
            throw new SecurityException("사용자의 권한이 부족합니다.");
        }


    }

}
