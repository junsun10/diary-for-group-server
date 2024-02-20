package com.example.diary.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PostCreateDto {

//    @NotNull
//    private Long memberId;

    @NotBlank
    private String title;

    @NotBlank
    private String body;
}