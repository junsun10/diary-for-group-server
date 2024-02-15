package com.example.diary.domain.group;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "groups")
public class Group {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    private String name;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();

    public Group(String name) {
        this.name = name;
        this.createdDate = LocalDateTime.now();
    }

    //TODO: 연관관계 메소드
}

