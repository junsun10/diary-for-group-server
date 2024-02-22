package com.example.diary.domain.group;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_leader_id")
    private Member groupLeader;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE)
    private List<GroupMember> groupMembers = new ArrayList<>();

    public Group(GroupCreateDto groupCreateDto, Member groupLeader) {
        this.name = groupCreateDto.getName();
        this.groupLeader = groupLeader;
        this.createdDate = LocalDateTime.now();
    }
}

