package com.example.diary.controller;

import com.example.diary.domain.post.Comment;
import com.example.diary.dto.comment.CommentCreateDto;
import com.example.diary.dto.comment.CommentDto;
import com.example.diary.dto.comment.CommentUpdateDto;
import com.example.diary.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 내가 작성한 댓글 목록
     */
    @GetMapping("/member/{memberId}")
    public List<CommentDto> myComments(@PathVariable @Valid Long memberId) {
        List<Comment> comments = commentService.getMyCommentList(memberId);
        List<CommentDto> commentDtos = comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return commentDtos;
    }

    /**
     * 일기 댓글 목록
     */
    @GetMapping("/post/{postId}")
    public List<CommentDto> postComments(@PathVariable @Valid Long postId) {
        List<Comment> comments = commentService.getPostCommentList(postId);
        List<CommentDto> commentDtos = comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return commentDtos;
    }

    /**
     * 댓글 생성
     */
    @PostMapping("/new")
    public Long create(@RequestBody @Valid CommentCreateDto commentCreateDto) {
        Long id = commentService.create(commentCreateDto);
        return id;
    }

    /**
     * 댓글 수정
     */
    @PostMapping("/{commentId}/edit")
    public Long update(@PathVariable @Valid Long commentId, @RequestBody @Valid CommentUpdateDto commentUpdateDto) {
        commentService.update(commentUpdateDto);
        return commentUpdateDto.getId();
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}/remove")
    public Long remove(@PathVariable @Valid Long commentId) {
        commentService.remove(commentId);
        return commentId;
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto(
                comment.getId(),
                comment.getMember().getId(),
                comment.getPost().getId(),
                comment.getBody(),
                comment.getCreatedDate(),
                comment.getChangedDate()
        );
        return commentDto;
    }
}
