package com.example.diary.repository;

import com.example.diary.domain.post.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByMemberIdAndPostId(Long memberId, Long postId);
}
