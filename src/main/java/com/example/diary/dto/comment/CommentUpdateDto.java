package com.example.diary.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentUpdateDto {

    @NotNull
    private Long id;

//    @NotNull
//    private Long memberId;

    @NotBlank
    private String body;
}
