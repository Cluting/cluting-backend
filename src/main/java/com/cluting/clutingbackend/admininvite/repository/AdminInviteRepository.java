package com.cluting.clutingbackend.admininvite.repository;

import com.cluting.clutingbackend.admininvite.domain.AdminInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminInviteRepository extends JpaRepository<AdminInvite, Long> {
}
