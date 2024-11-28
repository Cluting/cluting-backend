package com.cluting.clutingbackend.docEval.repository;

import com.cluting.clutingbackend.evaluate.domain.Criteria;
import com.cluting.clutingbackend.plan.domain.Application;
import com.cluting.clutingbackend.part.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Criteria2Repository extends JpaRepository<Criteria, Long> {
    Optional<Criteria> findByApplicationAndPartAndName(Application application, Part part, String name);


    // 특정 applicationId, clubUserId, partId에 해당하는 Criteria 조회
    List<Criteria> findByApplication_IdAndClubUser_IdAndPart_Id(
            Long applicationId, Long clubUserId, Long partId);

}