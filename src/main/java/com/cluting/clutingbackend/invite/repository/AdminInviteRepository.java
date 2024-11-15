package com.cluting.clutingbackend.invite.repository;

import com.cluting.clutingbackend.invite.domain.AdminInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdminInviteRepository extends JpaRepository<AdminInvite, Long> {
    Optional<AdminInvite> findByUniqueInviteTokenAndIsUsedFalse(String token);
}
