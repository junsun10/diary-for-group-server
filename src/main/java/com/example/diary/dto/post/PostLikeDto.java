package com.example.diary.dto.post;

import com.example.diary.domain.post.PostLike;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeDto {

//    @NotNull
//    private Long memberId;  //좋아요 누른 member

    @NotNull
    private Long postId;    //좋아요 누른 post

    public PostLikeDto(PostLike postLike) {
//        this.memberId = postLike.getMember().getId();
        this.postId = postLike.getPost().getId();
    }
}
