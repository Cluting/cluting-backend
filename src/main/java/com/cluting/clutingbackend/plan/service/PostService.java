package com.cluting.clutingbackend.plan.service;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.dto.response.PostResponseDto;
import com.cluting.clutingbackend.plan.dto.response.PostsResponseDto;
import com.cluting.clutingbackend.plan.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 동아리 공고 리스트
    @Transactional(readOnly = true)
    public PostsResponseDto getPosts(int pageNum, String sortType, Club.Type clubType, Club.Category fieldType) {
        List<Post> posts = postRepository.findPosts(pageNum, sortType, clubType, fieldType);
        return new PostsResponseDto((long) posts.size(), posts.stream().map(PostResponseDto::toDto).toList());
    }
}
