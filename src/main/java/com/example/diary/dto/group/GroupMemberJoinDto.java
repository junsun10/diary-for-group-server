package com.example.diary.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberJoinDto {

    @NotNull
    private Long groupId;

    @NotNull
    private Long memberId;

    public GroupMemberJoinDto(Long groupId, Long memberId) {
        this.groupId = groupId;
        this.memberId = memberId;
    }
}
