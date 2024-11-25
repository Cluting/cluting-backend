package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

//    List<Part> findByPost_PostId(Long postId);

    List<Part> findByPostId(Long postId);
    Optional<Part> findByNameAndPostId(String name, Long postId);
}
