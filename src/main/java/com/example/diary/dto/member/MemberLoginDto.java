package com.example.diary.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginDto {

    @NotBlank
    private String name;

    @NotBlank
    private String password;
}

