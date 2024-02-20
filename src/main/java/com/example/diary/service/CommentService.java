package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.comment.CommentCreateDto;
import com.example.diary.dto.comment.CommentDto;
import com.example.diary.dto.comment.CommentUpdateDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.CommentRepository;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    /**
     * 내가 작성한 댓글 목록 조회
     */
    public List<CommentDto> getMyCommentList(Long memberId) {

        List<Comment> comments = commentRepository.findByMemberId(memberId);
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(comment))
                .collect(Collectors.toList());

        return commentDtos;
    }

    /**
     * 일기 댓글 목록 조회
     */
    public List<CommentDto> getPostCommentList(Long postId) {

        postIsExist(postId);

        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> new CommentDto(comment))
                .collect(Collectors.toList());

        return commentDtos;
    }

    /**
     * 댓글 등록
     */
    @Transactional
    public CommentDto create(CommentCreateDto commentCreateDto, Long memberId) {

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        Post post = postIsExist(commentCreateDto.getPostId());

        Comment comment = new Comment(member, post, commentCreateDto.getBody());
        commentRepository.save(comment);
        CommentDto commentDto = new CommentDto(comment);

        return commentDto;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentDto update(CommentUpdateDto commentUpdateDto, Long memberId) {

        Comment comment = commentIsExist(commentUpdateDto.getId());
        comment.update(commentUpdateDto);
        CommentDto commentDto = new CommentDto(comment);

        return commentDto;
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public boolean remove(Long memberId, Long commentId) {

        Comment comment = commentIsExist(commentId);

        commentIsAuthor(memberId, commentId);
        commentRepository.delete(comment);

        return true;
    }

    /**
     * 일기 존재 확인
     */
    public Post postIsExist(Long postId) {

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 일기입니다.", 0);
        }

        Post post = postOptional.get();

        return post;
    }

    /**
     * 댓글 존재 확인
     */
    public Comment commentIsExist(Long commentId) {

        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        if (commentOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 일기입니다.", 0);
        }

        Comment comment = commentOptional.get();

        return comment;
    }

    /**
     * 댓글 권한 확인
     */
    public void commentIsAuthor(Long memberId, Long commentId) {

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.get();

        if (comment.getMember().getId() != memberId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}
