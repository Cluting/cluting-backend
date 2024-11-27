package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Part2Repository extends JpaRepository<Part, Long> {
    @Query("SELECT p FROM Part p WHERE p.post.id = :postId")
    List<Part> findByPostId(Long postId);
}