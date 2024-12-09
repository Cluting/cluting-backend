package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface GroupRepository extends JpaRepository<Group,Long> {

    Optional<Group> findByRecruitIdAndName(Long recruitId, String partName);

    // [계획하기] 불러오기
    List<Group> findByRecruitId(Long recruitId);

    // [계획하기] 수정할 때 이미 있는 컬럼을 삭제 후 새로 추가하기 위함.
    void deleteByRecruitId(Long recruitId);

}
