package com.example.diary.domain.post;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.post.PostUpdateDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String title;

    private String body;

    private Long view;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

    public Post(Member member, String title, String body) {
        this.member = member;
        this.title = title;
        this.body = body;
        this.view = 0L;
        this.createdDate = LocalDateTime.now();
        this.changedDate = LocalDateTime.now();
    }

    /**
     * 일기 수정
     */
    public void update(PostUpdateDto postUpdateDto) {
        if (postUpdateDto.getTitle() != null) {
            this.title = postUpdateDto.getTitle();
        }
        if (postUpdateDto.getBody() != null) {
            this.body = postUpdateDto.getBody();
        }
        this.changedDate = LocalDateTime.now();
    }

    /**
     * 조회수 증가
     */
    public void addView() {
        this.view += 1;
    }
}
