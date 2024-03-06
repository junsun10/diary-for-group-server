package com.example.diary.dto.group;

import com.example.diary.domain.member.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreateDto {

    @NotEmpty
    private String name;

    public GroupCreateDto(String name) {
        this.name = name;
    }
}
