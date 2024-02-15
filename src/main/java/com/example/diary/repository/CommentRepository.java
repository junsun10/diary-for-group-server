package com.example.diary.repository;

import com.example.diary.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByMemberId(Long memberId);
    List<Comment> findByPostId(Long postId);
}
