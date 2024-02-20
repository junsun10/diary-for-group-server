package com.example.diary.domain.group;

import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
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

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<GroupMember> groupMembers = new ArrayList<>();

    public Group(GroupCreateDto groupCreateDto) {
        this.name = groupCreateDto.getName();
        this.createdDate = LocalDateTime.now();
    }

    //TODO: 연관관계 메소드
}

