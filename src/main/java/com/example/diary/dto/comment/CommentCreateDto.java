package com.example.diary.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateDto {

//    @NotNull
//    private Long memberId; //댓글 작성자

    @NotNull
    private Long postId; //댓글 단 post

    @NotBlank
    private String body; //댓글

    public CommentCreateDto(Long postId, String body) {
        this.postId = postId;
        this.body = body;
    }
}
