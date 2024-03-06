package com.example.diary.controller;

import com.example.diary.dto.comment.CommentCreateDto;
import com.example.diary.dto.comment.CommentDto;
import com.example.diary.dto.comment.CommentUpdateDto;
import com.example.diary.service.CommentService;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 내가 작성한 댓글 목록
     */
    @GetMapping("/my")
    public ResponseEntity<List<CommentDto>> myComments(
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<CommentDto> commentDtos = commentService.getMyCommentList(memberId);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    /**
     * 일기 댓글 목록
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> postComments(
            @PathVariable @Valid Long postId) {
        List<CommentDto> commentDtos = commentService.getPostCommentList(postId);
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }

    /**
     * 댓글 생성
     */
    @PostMapping("/new")
    public ResponseEntity<CommentDto> create(
            @RequestBody @Valid CommentCreateDto commentCreateDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        CommentDto commentDto = commentService.create(commentCreateDto, memberId);
        return new ResponseEntity<>(commentDto, HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     */
    @PostMapping("/{commentId}/edit")
    public ResponseEntity<CommentDto> update(
            @PathVariable @Valid Long commentId, @RequestBody @Valid CommentUpdateDto commentUpdateDto,
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        CommentDto commentDto = commentService.update(commentUpdateDto, memberId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}/remove")
    public ResponseEntity remove(
            @PathVariable @Valid Long commentId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        commentService.remove(memberId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
