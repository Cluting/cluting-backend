package com.cluting.clutingbackend.plan.repository.customRepository.customRepositoryImpl;

import com.cluting.clutingbackend.plan.domain.Club;
import com.cluting.clutingbackend.plan.domain.Post;
import com.cluting.clutingbackend.plan.repository.customRepository.PostCustomRepository;
import com.cluting.clutingbackend.util.StaticValue;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cluting.clutingbackend.plan.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> findPosts(Integer pageNum, String sortType, Club.Type clubType, Club.Category fieldType) {
        Pageable pageable = PageRequest.of(pageNum, 12);

        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        clubTypeEq(clubType),
                        fieldTypeEq(fieldType)
                )
                .orderBy(sortBy(sortType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private com.querydsl.core.types.OrderSpecifier<?> sortBy(String sortType) {
        if ("deadline".equalsIgnoreCase(sortType)) {
            return post.deadLine.asc();
        } else if ("oldest".equalsIgnoreCase(sortType)) {
            return post.createdAt.asc();
        } else { // "newest" or default
            return post.createdAt.desc();
        }
    }

    private BooleanExpression clubTypeEq(Club.Type clubType) {
        return clubType == null ? null : post.club.type.eq(clubType);
    }

    private BooleanExpression fieldTypeEq(Club.Category fieldType) {
        return fieldType == null ? null : post.club.category.eq(fieldType);
    }
}
