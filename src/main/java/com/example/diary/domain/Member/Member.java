package com.example.diary.domain.Member;

import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.domain.post.PostLike;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
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
}
