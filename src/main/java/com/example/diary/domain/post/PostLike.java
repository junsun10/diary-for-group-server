package com.example.diary.domain.post;

import com.example.diary.domain.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class PostLike {

    @Id @GeneratedValue
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
