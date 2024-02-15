package com.example.diary.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberCreateDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;

    @Email
    private String email;

    private LocalDateTime createdDate;
}
