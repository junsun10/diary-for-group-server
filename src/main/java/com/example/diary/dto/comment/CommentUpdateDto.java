package com.example.diary.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateDto {

    @NotNull
    private Long id;

    @NotBlank
    private String body;

    public CommentUpdateDto(Long id, String body) {
        this.id = id;
        this.body = body;
    }
}
