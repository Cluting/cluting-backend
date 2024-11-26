package com.cluting.clutingbackend.part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

//    List<Part> findByPost_PostId(Long postId);

    List<Part> findByPostId(Long postId);

    List<Part> findByPost_Id(Long applicationId);
}
