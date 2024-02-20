package com.example.diary.dto.group;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberCreateDto {

    @NotNull
    private Long groupId;

    public GroupMemberCreateDto(Long groupId) {
        this.groupId = groupId;
    }
}
