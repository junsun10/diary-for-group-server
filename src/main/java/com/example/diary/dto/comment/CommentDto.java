package com.example.diary.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    @NotNull
    private Long id;

    @NotNull
    private Long memberId; //댓글 작성자

    @NotNull
    private Long postId; //댓글 단 post

    @NotNull
    private String body; //댓글

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    public CommentDto(Long id, Long memberId, Long postId, String body, LocalDateTime createdDate, LocalDateTime changedDate) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
        this.body = body;
        this.createdDate = createdDate;
        this.changedDate = changedDate;
    }
}
