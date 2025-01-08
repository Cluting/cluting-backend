package com.cluting.clutingbackend.global.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal; // 사용자 정보 (예: CustomUserDetails)
    private Object credentials; // 인증 정보 (일반적으로 비밀번호, 지금은 필요 없음)

    public CustomAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(true); // 인증된 상태로 설정
    }

    public CustomAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        this(principal, null, authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

