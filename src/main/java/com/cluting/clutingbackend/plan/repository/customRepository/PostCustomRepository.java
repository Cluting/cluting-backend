package com.cluting.clutingbackend.plan.repository.customRepository;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.Post;

import java.util.List;

public interface PostCustomRepository {
    List<Post> findPosts(Integer pageNum, String sortType, Club.Type clubType, Club.Category fieldType);
}
