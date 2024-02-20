package com.example.diary.dto.group;

import com.example.diary.domain.group.GroupMember;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberDto {

    @NotNull
    private Long id;

    @NotNull
    private Long memberId;

    @NotNull
    private Long groupId;

    public GroupMemberDto(GroupMember groupMember) {
        this.id = groupMember.getId();
        this.memberId = groupMember.getMember().getId();
        this.groupId = groupMember.getGroup().getId();
    }
}
