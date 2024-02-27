package com.example.diary.domain.member;

import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.domain.post.PostLike;
import com.example.diary.dto.member.MemberCreateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Column(unique = true)
    private String name;

    @NotBlank
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<PostLike> likes = new ArrayList<>();

    public Member(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }

    public Member(MemberCreateDto memberCreateDto) {
        this.name = memberCreateDto.getName();
        this.password = memberCreateDto.getPassword();
        this.email = memberCreateDto.getEmail();
        this.createdDate = LocalDateTime.now();
    }
}
