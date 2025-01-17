package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.global.enums.Stage;
import com.cluting.clutingbackend.interview.domain.Interview;
import com.cluting.clutingbackend.interview.domain.InterviewEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewEvaluatorRepository extends JpaRepository<InterviewEvaluator, Long> {
    List<InterviewEvaluator> findByInterviewIdIn(List<Long> interviewIds);
    int countDistinctByInterviewId(Long interviewId);
    List<InterviewEvaluator> findByInterview(Interview interview);
    Optional<InterviewEvaluator> findFirstByInterviewId(Long interviewId);
    List<InterviewEvaluator> findByInterviewId(Long interviewId);
    Optional<InterviewEvaluator> findByInterviewIdAndClubUserId(Long interviewId, Long clubUserId);
    List<InterviewEvaluator> findAllByInterviewIdIn(List<Long> interviewIds);
    InterviewEvaluator findByGroupId(Long groupId);

//    List<InterviewEvaluator> findByInterviewId(Long applicationId);

    @Query("SELECT ie FROM InterviewEvaluator ie WHERE ie.interview.id = :interviewId")
    InterviewEvaluator findByInterview_Id(@Param("interviewId") Long interviewId);

    // recruitId와 stage에 따라 InterviewEvaluator를 조회
    @Query(value = "SELECT ie.* FROM tb_interview_evaluator ie " +
            "JOIN tb_interview i ON ie.interview_id = i.id " +
            "JOIN tb_application a ON i.application_id = a.id " +
            "WHERE a.recruit_id = :recruitId AND ie.stage = :stage", nativeQuery = true)
    List<InterviewEvaluator> findByRecruitIdAndStage(@Param("recruitId") Long recruitId, @Param("stage") Stage stage);
}

