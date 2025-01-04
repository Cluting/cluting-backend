package com.cluting.clutingbackend.user.repository;

import com.cluting.clutingbackend.user.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @Query(value =
            "SELECT r.club_id, COUNT(*) AS scrapCount " +
            "FROM tb_scrap s " +
            "JOIN tb_recruit r ON s.recruit_id = r.id " +
            "JOIN tb_club c ON r.club_id = c.id " +
            "WHERE c.is_recruiting = true " +
            "GROUP BY r.club_id " +
            "ORDER BY scrapCount DESC " +
            "LIMIT 3",
            nativeQuery = true)
    List<Object[]> findAllRecruiting();
}
