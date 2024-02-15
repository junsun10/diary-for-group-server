package com.example.diary.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostLikeDto {

    @NotNull
    private Long memberId;  //좋아요 누른 member

    @NotNull
    private Long postId;    //좋아요 누른 post
}
