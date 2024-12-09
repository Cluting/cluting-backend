package com.cluting.clutingbackend.prep.repository;

import com.cluting.clutingbackend.prep.domain.PrepStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrepStageRepository extends JpaRepository<PrepStage, Long> {
    // [계획하기] 불러오기
    List<PrepStage> findByRecruitId(Long recruitId);
    // [계획하기] 수정할 때 이미 있는 컬럼을 삭제 후 새로 추가하기 위함.
    void deleteByRecruitId(Long recruitId);
}