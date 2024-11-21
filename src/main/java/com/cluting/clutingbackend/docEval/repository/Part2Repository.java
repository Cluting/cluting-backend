package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.plan.domain.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Part2Repository extends JpaRepository<Part, Long> {
    List<Part> findByPostId(Long postId);
}