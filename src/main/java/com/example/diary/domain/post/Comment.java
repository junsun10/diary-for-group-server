package com.example.diary.domain.post;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.comment.CommentUpdateDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String body;

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    public Comment(Member member, Post post, String body) {
        this.member = member;
        this.post = post;
        this.body = body;
        this.createdDate = LocalDateTime.now();
        this.changedDate = LocalDateTime.now();
    }

    /**
     * 댓글 수정
     */
    public void update(CommentUpdateDto commentUpdateDto) {
        if (commentUpdateDto.getBody() != null) {
            this.body = commentUpdateDto.getBody();
        }
        this.changedDate = LocalDateTime.now();
    }
}
