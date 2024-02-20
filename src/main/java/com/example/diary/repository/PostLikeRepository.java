package com.example.diary.repository;

import com.example.diary.domain.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberIdAndPostId(Long memberId, Long postId);
    Optional<List<PostLike>> findByPostId(Long postId);
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
