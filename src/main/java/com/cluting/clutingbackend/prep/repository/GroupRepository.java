package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.prep.domain.RecruitGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<RecruitGroup, Long> {
    List<RecruitGroup> findByPostPostId(Long postId);

}
