package com.cluting.clutingbackend.admininvite.repository;

import com.cluting.clutingbackend.admininvite.domain.AdminInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminInviteRepository extends JpaRepository<AdminInvite, Long> {
    // [운영진 초대] 토큰으로 초대 정보 찾기
    Optional<AdminInvite> findByUniqueInviteToken(String token);
}
