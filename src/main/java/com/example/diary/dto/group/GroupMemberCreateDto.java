package com.example.diary.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupMemberCreateDto {

    @NotBlank
    private String groupName;

    public GroupMemberCreateDto(String groupName) {
        this.groupName = groupName;
    }
}
