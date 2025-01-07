package com.cluting.clutingbackend.user.repository;

import com.cluting.clutingbackend.user.domain.Recent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecentRepository extends JpaRepository<Recent, Long> {
}
