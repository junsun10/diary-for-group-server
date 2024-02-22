package com.example.diary.controller;

import com.example.diary.dto.post.PostLikeCreateDto;
import com.example.diary.dto.post.PostLikeDto;
import com.example.diary.service.PostLikeService;
import com.example.diary.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/post-likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    /**
     * 좋아요 등록
     */
    @PostMapping("")
    public ResponseEntity<PostLikeDto> like(
            @RequestBody @Valid PostLikeCreateDto postLikeCreateDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        PostLikeDto postLikeDto = postLikeService.add(postLikeCreateDto, memberId);
        return new ResponseEntity<>(postLikeDto, HttpStatus.CREATED);
    }

    /**
     * 좋아요 취소
     */
    @DeleteMapping("post/{postId}")
    public ResponseEntity unlike(
            @PathVariable @Valid Long postId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        postLikeService.remove(memberId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 단일 일기 좋아요 목록
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<PostLikeDto>> list(
            @PathVariable @Valid Long postId) {
        List<PostLikeDto> postLikeDtos = postLikeService.list(postId);
        return new ResponseEntity<>(postLikeDtos, HttpStatus.OK);
    }

    /**
     * 단일 일기에 대한 나의 좋아요 등록 여부
     */
    @GetMapping("/my-status/post/{postId}")
    public ResponseEntity<Boolean> status(
            @PathVariable @Valid Long postId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        boolean status = postLikeService.status(memberId, postId);

        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
