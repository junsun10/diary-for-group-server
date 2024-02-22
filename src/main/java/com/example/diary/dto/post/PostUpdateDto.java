package com.example.diary.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateDto {

    @NotNull
    private Long id;

//    @NotNull
//    private Long memberId;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    public PostUpdateDto(Long id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }
}
