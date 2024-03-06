package com.example.diary.dto.post;

import com.example.diary.domain.post.PostLike;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeDto {

//    @NotNull
//    private Long memberId;  //좋아요 누른 member

    @NotBlank
    private String memberName; //좋아요 누른 member name

    @NotNull
    private Long postId;    //좋아요 누른 post

    public PostLikeDto(PostLike postLike) {
//        this.memberId = postLike.getMember().getId();
        this.memberName = postLike.getMember().getName();
        this.postId = postLike.getPost().getId();
    }
}
