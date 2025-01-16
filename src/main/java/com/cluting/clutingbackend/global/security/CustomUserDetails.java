package com.cluting.clutingbackend.global.security;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;
    private final ClubUser selectedClubUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (selectedClubUser == null || selectedClubUser.getPermissionLevels() == null) {
            return List.of(); // 기본 권한 없음
        }
        return selectedClubUser.getPermissionLevels().stream()
                .map(permission -> (GrantedAuthority) permission.getPermissionLevel()::name)
                .toList();
    }



    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this.user;
    }
}
