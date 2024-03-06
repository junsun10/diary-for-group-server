package com.example.diary.controller;

import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostUpdateDto;
import com.example.diary.service.PostService;
import com.example.diary.session.SessionConst;
import com.example.diary.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    /**
     * 내 일기 목록
     */
    @GetMapping("/my")
    public ResponseEntity<List<PostDto>> myPostList(HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<PostDto> postDtos = postService.getList(memberId);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    /**
     * 단일 그룹 일기 목록
     */
    @GetMapping("/my-group/{groupId}")
    public ResponseEntity<List<PostDto>> myGroupPostList(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<PostDto> postDtos = postService.getGroupPostList(memberId, groupId);
        return new ResponseEntity<>(postDtos, HttpStatus.OK);
    }

    /**
     * 일기 생성
     */
    @PostMapping("/new")
    public ResponseEntity<PostDto> create(
            @RequestBody @Valid PostCreateDto postCreateDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        PostDto postDto = postService.create(postCreateDto, memberId);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    /**
     * 단일 일기 조회
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> get(
            @PathVariable @Valid Long postId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        PostDto postDto = postService.get(memberId, postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * 일기 수정
     */
    @PostMapping("/{postId}/edit")
    public ResponseEntity<PostDto> update(
            @PathVariable @Valid Long postId, @RequestBody @Valid PostUpdateDto postUpdateDto,
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        PostDto postDto = postService.update(postUpdateDto, memberId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    /**
     * 일기 삭제
     */
    @DeleteMapping("/{postId}/remove")
    public ResponseEntity remove(
            @PathVariable @Valid Long postId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        postService.remove(memberId, postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
