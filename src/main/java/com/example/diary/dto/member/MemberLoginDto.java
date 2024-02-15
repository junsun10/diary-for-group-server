package com.example.diary.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class MemberLoginDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}

