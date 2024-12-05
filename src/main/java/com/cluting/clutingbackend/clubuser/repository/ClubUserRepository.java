package com.cluting.clutingbackend.clubuser.repository;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {
    List<ClubUser> findByUser_Id(Long userId);
}
