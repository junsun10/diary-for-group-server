package com.example.diary.dto.post;

import com.example.diary.domain.post.PostLike;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeCreateDto {

    @NotNull
    private Long postId;    //좋아요 누른 post

    public PostLikeCreateDto(Long postId) {
        this.postId = postId;
    }
}
