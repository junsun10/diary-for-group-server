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

    @NotNull
    private Long groupId;

//    @NotBlank
    @NotNull
    private String title;

//    @NotBlank
    @NotNull
    private String body;

    public PostUpdateDto(Long id, Long groupId, String title, String body) {
        this.id = id;
        this.groupId = groupId;
        this.title = title;
        this.body = body;
    }
}
