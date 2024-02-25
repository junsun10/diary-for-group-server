package com.example.diary.dto.group;

import com.example.diary.domain.group.GroupMember;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String memberName;

    @NotNull
    private Long groupId;

    @NotNull
    private boolean status;

    public GroupMemberDto(GroupMember groupMember) {
        this.id = groupMember.getId();
        this.memberId = groupMember.getMember().getId();
        this.memberName = groupMember.getMember().getName();
        this.groupId = groupMember.getGroup().getId();
        this.status = groupMember.getStatus();
    }
}
