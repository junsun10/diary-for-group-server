package com.example.diary.dto.member;

import com.example.diary.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.name = member.getName();
    }
}
