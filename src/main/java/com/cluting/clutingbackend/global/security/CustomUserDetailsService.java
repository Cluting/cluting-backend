package com.cluting.clutingbackend.global.security;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import com.cluting.clutingbackend.clubuser.repository.ClubUserRepository;
import com.cluting.clutingbackend.global.security.CustomUserDetails;
import com.cluting.clutingbackend.user.domain.User;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ClubUserRepository clubUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email)
                );
        ClubUser clubUser = clubUserRepository.findFirstByUserId(user.getId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email)
                );

        return CustomUserDetails.builder()
                .selectedClubUser(clubUser)
                .user(user)
                .build();
    }

    public UserDetails loadUserByUserId(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() ->
                        new UsernameNotFoundException("사용자를 찾을 수 없습니다. 이메일: " + email)
                );
        ClubUser clubUser = clubUserRepository.findFirstByUserId(user.getId())
                .orElseThrow(() ->
                        new UsernameNotFoundException("동아리 사용자를 찾을 수 없습니다. 이메일: " + email)
                );

        return CustomUserDetails.builder()
                .selectedClubUser(clubUser)
                .user(user)
                .build();
    }
}
