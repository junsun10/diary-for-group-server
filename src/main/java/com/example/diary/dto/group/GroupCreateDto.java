package com.example.diary.dto.group;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupCreateDto {

    @NotEmpty
    private String name;
}
