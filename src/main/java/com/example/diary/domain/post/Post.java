package com.example.diary.domain.post;

import com.example.diary.domain.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Member member; // 작성자

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String title;

    private String body;

    private Long view;

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();

    private LocalDateTime created_date;

    private LocalDateTime changed_date;

}
