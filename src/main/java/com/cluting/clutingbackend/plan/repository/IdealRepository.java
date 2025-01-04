package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Ideal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdealRepository extends JpaRepository<Ideal,Long> {
    List<Ideal> findByGroupId(Long groupId);
}
