package com.example.diary.controller;

import com.example.diary.dto.post.PostLikeDto;
import com.example.diary.service.PostLikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/postlikes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * 좋아요 등록
     */
    @PostMapping("")
    public Long like(@RequestBody @Valid PostLikeDto postLikeDto) {
        postLikeService.add(postLikeDto);
        return postLikeDto.getPostId();
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("/{memberId}/{postId}")
    public void unlike(@PathVariable @Valid Long memberId,
                       @PathVariable @Valid Long postId) {
        postLikeService.remove(memberId, postId);
    }
}
