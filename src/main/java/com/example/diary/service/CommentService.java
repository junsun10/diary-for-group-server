package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.comment.CommentCreateDto;
import com.example.diary.dto.comment.CommentUpdateDto;
import com.example.diary.repository.CommentRepository;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    /**
     * 댓글 등록
     */
    @Transactional
    public Long create(CommentCreateDto commentCreateDto) {
        Optional<Member> memberOptional = memberRepository.findById(commentCreateDto.getMemberId());
        Member member = memberOptional.get();

        Optional<Post> postOptional = postRepository.findById(commentCreateDto.getPostId());
        Post post = postOptional.get();

        Comment comment = new Comment(member, post, commentCreateDto.getBody());
        commentRepository.save(comment);

        return comment.getId();
    }

    /**
     * 내 댓글 목록 조회
     */
    public List<Comment> getMyCommentList(Long memberId) {
        List<Comment> comments = commentRepository.findByMemberId(memberId);
        return comments;
    }

    /**
     * 일기 댓글 목록 조회
     */
    public List<Comment> getPostCommentList(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments;
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public Long update(CommentUpdateDto commentUpdateDto) {
        Optional<Comment> commentOptional = commentRepository.findById(commentUpdateDto.getId());
        Comment comment = commentOptional.get();
        comment.update(commentUpdateDto);
        return comment.getId();
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public Long remove(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Comment comment = commentOptional.get();
        commentRepository.delete(comment);
        return comment.getId();
    }


}
