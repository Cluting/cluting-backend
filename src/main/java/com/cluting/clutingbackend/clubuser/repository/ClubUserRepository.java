package com.cluting.clutingbackend.clubuser.repository;

import com.cluting.clutingbackend.clubuser.domain.ClubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubUserRepository extends JpaRepository<ClubUser, Long> {
    // [리크루팅 홈] 운영진 가져오기
    @Query("""
    SELECT cu\s
    FROM ClubUser cu\s
    JOIN Recruit r ON cu.club.id = r.club.id\s
    WHERE cu.club.id = :clubId\s
      AND cu.generation = r.generation\s
      AND cu.role = 'STAFF'
   \s""")
    List<ClubUser> findStaffByClubIdAndGeneration(Long clubId);

    // [리크루팅 홈] 해당 운영진 정보 가져오기
    @Query("SELECT cu FROM ClubUser cu WHERE cu.club.id = :clubId AND cu.user.id = :clubUserId")
    ClubUser findByClubIdAndUserId(Long clubId, Long clubUserId);
}
