package com.example.diary.domain.post;

import com.example.diary.domain.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Member member; // 작성자

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String body;

    private LocalDateTime createdDate;

    private LocalDateTime changedDate;

}
