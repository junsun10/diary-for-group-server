package com.example.diary.dto.group;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class GroupDto {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

    private LocalDateTime createdDate;

    private Long groupLeaderId;

    private List<Long> groupMembersId; //그룹 멤버 Id

    private List<String> groupMembersName; //그룹 멤버 이름

    public GroupDto(Long id, String name, LocalDateTime createdDate, List<GroupMember> groupMembers) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.groupMembersId = groupMembers.stream()
                .map(m -> m.getMember().getId())
                .collect(Collectors.toList());
        this.groupMembersName = groupMembers.stream()
                .map(m -> m.getMember().getName())
                .collect(Collectors.toList());
    }

    public GroupDto(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.createdDate = group.getCreatedDate();
        this.groupLeaderId = group.getGroupLeader().getId();
        this.groupMembersId = group.getGroupMembers().stream()
                .map(groupMember -> groupMember.getMember().getId())
                .collect(Collectors.toList());
        this.groupMembersName = group.getGroupMembers().stream()
                .map(groupMember -> groupMember.getMember().getName())
                .collect(Collectors.toList());
    }
}
