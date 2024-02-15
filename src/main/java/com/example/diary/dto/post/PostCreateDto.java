package com.example.diary.dto.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateDto {

    @NotNull
    private Long memberId;

    @NotEmpty
    private String title;

    private String body;
}