package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.prep.domain.PrepStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrepStageRepository extends JpaRepository<PrepStage, Long> {
    List<PrepStage> findByPost_PostIdOrderByStageOrderAsc(Long postId);
    List<PrepStage> findByPost_PostId(Long postId);

}