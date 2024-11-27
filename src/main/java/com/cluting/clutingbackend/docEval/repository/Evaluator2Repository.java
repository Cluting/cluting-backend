package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.evaluate.domain.Evaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Evaluator2Repository extends JpaRepository<Evaluator, Long> {
    @Query("SELECT COUNT(DISTINCT c.id) " +
        "FROM Evaluator e JOIN e.evaluators c " +
        "WHERE e.part.name = :part AND e.part.post.id = :postId")
    Long countDistinctClubUsersByPartAndPostId(@Param("part") String part, @Param("postId") Long postId);

    List<Evaluator> findByPart_Id(Long partId);
}
