package com.example.diary.dto.post;

import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.domain.post.PostLike;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostDto {

    @NotNull
    private Long id;

//    @NotNull
//    private Long memberId;

    @NotNull
    private String memberName;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Long view;

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    private List<Long> comments; //댓글 Id

    private List<Long> likes; //좋아요 Id

    public PostDto(Post post) {
        this.id = post.getId();
//        this.memberId = post.getMember().getId();
        this.memberName = post.getMember().getName();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.view = post.getView();
        this.createdDate = post.getCreatedDate();
        this.changedDate = post.getChangedDate();
        this.comments = post.getComments().stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());
        this.likes = post.getLikes().stream()
                .map(l -> l.getId())
                .collect(Collectors.toList());
    }
}
