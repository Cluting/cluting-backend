package com.cluting.clutingbackend.interview.repository;

import com.cluting.clutingbackend.global.enums.QuestionType2;
import com.cluting.clutingbackend.interview.domain.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    List<InterviewQuestion> findByInterviewId(Long interviewId);
    List<InterviewQuestion> findByInterviewIdAndType(Long interview_id, QuestionType2 type);
    Optional<InterviewQuestion> findByInterviewIdAndIdAndType(Long interviewId, Long id, QuestionType2 type);

}
