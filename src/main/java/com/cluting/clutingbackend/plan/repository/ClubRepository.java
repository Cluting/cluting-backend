package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    @Query(value = "" +
            "SELECT * " +
            "FROM tb_club " +
            "ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<Club> findPopular();
}
