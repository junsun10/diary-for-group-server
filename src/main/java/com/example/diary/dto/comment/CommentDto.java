package com.example.diary.dto.comment;

import com.example.diary.domain.post.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentDto {

    @NotNull
    private Long id;

//    @NotNull
//    private Long memberId; //댓글 작성자 id

    @NotBlank
    private String memberName; //댓글 작성자 이름

    @NotNull
    private Long postId; //댓글 단 post

    @NotNull
    private String body; //댓글

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
//        this.memberId = comment.getMember().getId();
        this.memberName = comment.getMember().getName();
        this.postId = comment.getPost().getId();
        this.body = comment.getBody();
        this.createdDate = comment.getCreatedDate();
        this.changedDate = comment.getChangedDate();
    }
}
