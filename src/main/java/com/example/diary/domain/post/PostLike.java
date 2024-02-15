package com.example.diary.domain.post;

import com.example.diary.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
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

    public PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}
