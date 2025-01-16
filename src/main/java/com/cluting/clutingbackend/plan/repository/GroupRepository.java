package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface GroupRepository extends JpaRepository<Group,Long> {

    Optional<Group> findByRecruitIdAndName(Long recruitId, String partName);

    // [계획하기] 불러오기
    List<Group> findByRecruitId(Long recruitId);

    // [계획하기] 수정할 때 이미 있는 컬럼을 삭제 후 새로 추가하기 위함.
    @Modifying
    @Query("DELETE FROM Group g WHERE g.recruit.id = :recruitId")
    void deleteAllByRecruitId(@Param("recruitId") Long recruitId);

    @Query("SELECT g FROM Group g WHERE g.recruit.id = :recruitId")
    List<Group> findAllByRecruitId(@Param("recruitId") Long recruitId);
    @Query("""
    SELECT g FROM Group g\s
    WHERE g.recruit.id = :recruitId\s
    AND (g.isCommon = false OR (g.isCommon = true AND g.evalType = 'DOCUMENT'))
   \s""")
    List<Group> findAllByRecruitIdForDocument(@Param("recruitId") Long recruitId);
    @Query("""
    SELECT g FROM Group g\s
    WHERE g.recruit.id = :recruitId\s
    AND (g.isCommon = false OR (g.isCommon = true AND g.evalType = 'INTERVIEW'))
   \s""")
    List<Group> findAllByRecruitIdForInterview(@Param("recruitId") Long recruitId);

    Group findByRecruit_IdAndAndName(Long recruitId, String name);

    Optional<Group> findById(Long id);

    @Query("SELECT g FROM Group g WHERE g.name = :name AND g.recruit.id = :recruitId")
    Group findByNameAndRecruit_Id(@Param("recruitId") Long recruitId,
                                  @Param("name") String name);
}
