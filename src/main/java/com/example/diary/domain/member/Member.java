package com.example.diary.domain.member;

import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.domain.post.PostLike;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String password;

    private String email;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLike> likes = new ArrayList<>();

    public Member(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }
}
