package com.example.diary.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String password;

    @Email
    private String email;

    private LocalDateTime createdDate;

    public MemberCreateDto(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdDate = LocalDateTime.now();
    }
}
