package com.cluting.clutingbackend.part;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {

//    List<Part> findByPost_PostId(Long postId);

    List<Part> findByPostId(Long postId);
    Optional<Part> findByNameAndPostId(String name, Long postId);

    @Query("SELECT p FROM Part p WHERE p.name = :name AND p.id = :postId")
    Optional<Part> findUniqueByNameAndPostId(@Param("name") String name, @Param("postId") Long postId);

    List<Part> findByPost_Id(Long applicationId);
}
