package com.cluting.clutingbackend.plan.repository;

import com.cluting.clutingbackend.plan.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
