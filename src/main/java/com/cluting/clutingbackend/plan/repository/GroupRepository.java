package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group,Long> {

    Optional<Group> findByRecruitIdAndName(Long recruitId, String partName);


}
