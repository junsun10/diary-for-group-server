package com.example.diary.dto.post;

import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.PostLike;
import jakarta.validation.constraints.NotEmpty;
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

    @NotNull
    private Long memberId;

    @NotEmpty
    private String title;

    private String body;

    private Long view;

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    private List<Long> comments; //댓글 Id

    private List<Long> likes; //좋아요 Id

    public PostDto(Long id, Long memberId, String title, String body, Long view, LocalDateTime createdDate, LocalDateTime changedDate, List<Comment> comments, List<PostLike> likes) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.body = body;
        this.view = view;
        this.createdDate = createdDate;
        this.changedDate = changedDate;
        this.comments = comments.stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());
        this.likes = likes.stream()
                .map(l -> l.getId())
                .collect(Collectors.toList());
    }
}
