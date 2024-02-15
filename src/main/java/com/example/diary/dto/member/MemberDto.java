package com.example.diary.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDto {

    @NotNull
    private Long id;

    @NotEmpty
    private String name;

    public MemberDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
