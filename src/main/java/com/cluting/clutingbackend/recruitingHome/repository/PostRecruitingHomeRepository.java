package com.cluting.clutingbackend.recruitingHome.repository;

import com.cluting.clutingbackend.plan.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRecruitingHomeRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByClub_ClubIdAndPostId(Long clubId, Long postId);

}
