package com.example.diary.domain.group;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
public class Group {

    @Id @GeneratedValue
    @Column(name = "group_id")
    private Long id;

    private String name;

    private Long count;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();
}
