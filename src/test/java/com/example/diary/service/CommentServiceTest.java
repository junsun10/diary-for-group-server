package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Comment;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.comment.CommentCreateDto;
import com.example.diary.dto.comment.CommentDto;
import com.example.diary.dto.comment.CommentUpdateDto;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void 작성한_모든_댓글_목록() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto1 = new PostCreateDto("Title", "Content");
        PostCreateDto postCreateDto2 = new PostCreateDto("Title", "Content");
        Post post1 = new Post(member1, postCreateDto1);
        Post post2 = new Post(member1, postCreateDto2);
        postRepository.save(post1);
        postRepository.save(post2);

        CommentCreateDto commentCreateDto1 = new CommentCreateDto(post1.getId(), "Comment1");
        CommentCreateDto commentCreateDto2 = new CommentCreateDto(post2.getId(), "Comment2");
        commentService.create(commentCreateDto1, member2.getId());
        commentService.create(commentCreateDto2, member2.getId());

        // when
        List<CommentDto> commentList = commentService.getMyCommentList(member2.getId());

        // then
        assertThat(commentList.size()).isEqualTo(2);
        for (CommentDto commentDto : commentList) {
            assertThat(commentDto.getMemberName()).isEqualTo(member2.getName());
        }
    }

    @Test
    void 작성한_모든_댓글_목록_댓글없음() {
        // given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        // when
        List<CommentDto> commentList = commentService.getMyCommentList(member.getId());

        // then
        assertThat(commentList.isEmpty()).isTrue();
    }

    @Test
    void 일기_댓글_목록() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto1 = new CommentCreateDto(post.getId(), "Comment1");
        CommentCreateDto commentCreateDto2 = new CommentCreateDto(post.getId(), "Comment2");
        commentService.create(commentCreateDto1, member2.getId());
        commentService.create(commentCreateDto2, member2.getId());

        // when
        List<CommentDto> commentList = commentService.getPostCommentList(post.getId());

        // then
        assertThat(commentList.size()).isEqualTo(2);
        for (CommentDto commentDto : commentList) {
            assertThat(commentDto.getMemberName()).isEqualTo(member2.getName());
            assertThat(commentDto.getPostId()).isEqualTo(post.getId());
        }
    }

    @Test
    void 일기_댓글_목록_댓글없음() {
        // given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member, postCreateDto);
        postRepository.save(post);

        // when
        List<CommentDto> commentList = commentService.getPostCommentList(post.getId());

        // then
        assertThat(commentList.isEmpty()).isTrue();
    }

    @Test
    void 댓글_등록() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        // when
        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        // then
        assertThat(commentDto.getPostId()).isEqualTo(post.getId());
        assertThat(commentDto.getMemberName()).isEqualTo(member2.getName());
        assertThat(commentDto.getBody()).isEqualTo("Comment");
    }

    @Test
    void 댓글_등록_일기없음() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when
        CommentCreateDto commentCreateDto = new CommentCreateDto(100L, "Comment");
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> commentService.create(commentCreateDto, member2.getId()));

        // then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 댓글_수정() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        // when
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(commentDto.getId(), "new comment");
        CommentDto updatedCommentDto = commentService.update(commentUpdateDto, member2.getId());

        //then
        assertThat(updatedCommentDto.getMemberName()).isEqualTo(member2.getName());
        assertThat(updatedCommentDto.getBody()).isEqualTo("new comment");
    }

    @Test
    void 댓글_수정_댓글없음() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        // when
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(100L, "new comment");
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> commentService.update(commentUpdateDto, member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    void 댓글_수정_작성자아님() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        Member member3 = new Member("username3", "12345!", "user3@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        // when
        CommentUpdateDto commentUpdateDto = new CommentUpdateDto(commentDto.getId(), "new comment");
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> commentService.update(commentUpdateDto, member3.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 댓글_삭제() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        // when
        boolean remove = commentService.remove(member2.getId(), commentDto.getId());

        //then
        assertThat(remove).isTrue();
    }

    @Test
    void 댓글_삭제_댓글없음() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        // when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> commentService.remove(member2.getId(), 100L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    void 댓글_삭제_작성자아님() {
        // given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        Member member3 = new Member("username3", "12345!", "user3@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        // when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> commentService.remove(member3.getId(), commentDto.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 일기_존재_확인_일기있음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member, postCreateDto);
        postRepository.save(post);

        //when
        Post getPost = commentService.postIsExist(post.getId());

        assertThat(getPost.getId()).isEqualTo(post.getId());
        assertThat(getPost.getTitle()).isEqualTo("Title");
        assertThat(getPost.getBody()).isEqualTo("Content");
        assertThat(getPost.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 일기_존재_확인_일기없음() {
        //given

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> commentService.postIsExist(1L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 댓글_존재_확인_댓글있음() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        //when
        Comment comment = commentService.commentIsExist(commentDto.getId());

        assertThat(comment.getId()).isEqualTo(commentDto.getId());
        assertThat(comment.getBody()).isEqualTo("Comment");
        assertThat(comment.getMember().getId()).isEqualTo(member2.getId());
    }

    @Test
    void 댓글_존재_확인_댓글없음() {
        //given
        Member member = new Member("username1", "12345!", "user1@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member, postCreateDto);
        postRepository.save(post);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> commentService.commentIsExist(post.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 댓글입니다.");
    }

    @Test
    void 댓글_권한_확인_권한없음() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        Member member3 = new Member("username3", "12345!", "user3@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        Post post = new Post(member1, postCreateDto);
        postRepository.save(post);

        CommentCreateDto commentCreateDto = new CommentCreateDto(post.getId(), "Comment");
        CommentDto commentDto = commentService.create(commentCreateDto, member2.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> commentService.commentIsAuthor(member3.getId(), commentDto.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }
}