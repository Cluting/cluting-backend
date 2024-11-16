package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.repository.customRepository.PostCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {
}