package com.example.diary.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateDto {

//    @NotNull
//    private Long memberId;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public PostCreateDto(String title, String body) {
        this.title = title;
        this.body = body;
    }
}