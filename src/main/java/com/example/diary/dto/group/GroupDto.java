package com.example.diary.dto.group;

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

    private List<Long> groupMembers; //그룹 멤버 Id

    public GroupDto(Long id, String name, LocalDateTime createdDate, List<GroupMember> groupMembers) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.groupMembers = groupMembers.stream()
                .map(m -> m.getMember().getId())
                .collect(Collectors.toList());
    }
}
