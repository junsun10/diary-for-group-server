package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostUpdateDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 일기생성() {
        // given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        // when
        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member.getId());

        // then
        Optional<Post> savedPostOptional = postRepository.findById(postDto.getId());
        assertTrue(savedPostOptional.isPresent());

        Post savedPost = savedPostOptional.get();

        assertThat(savedPost.getId()).isEqualTo(postDto.getId());
        assertThat(savedPost.getTitle()).isEqualTo("Title");
        assertThat(savedPost.getBody()).isEqualTo("Content");
        assertThat(savedPost.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 단일_일기_조회() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member.getId());

        //when
        PostDto getPostDto = postService.get(member.getId(), postDto.getId());

        //then
        assertThat(getPostDto.getId()).isEqualTo(postDto.getId());
        assertThat(getPostDto.getTitle()).isEqualTo("Title");
        assertThat(getPostDto.getBody()).isEqualTo("Content");
        assertThat(getPostDto.getMemberName()).isEqualTo(member.getName());
        assertThat(getPostDto.getView()).isEqualTo(1);
    }

    @Test
    void 내_일기_목록_조회() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto1 = new PostCreateDto("Title1", "Content1");
        PostDto postDto1 = postService.create(postCreateDto1, member.getId());
        PostCreateDto postCreateDto2 = new PostCreateDto("Title2", "Content2");
        PostDto postDto2 = postService.create(postCreateDto2, member.getId());

        //when
        List<PostDto> postDtos = postService.getList(member.getId());

        //then
        assertThat(postDtos.size()).isEqualTo(2);

        for (PostDto postDto : postDtos) {
            assertThat(postDto.getMemberName()).isEqualTo(member.getName());
        }
    }

    @Test
    void 내_일기_목록_조회_일기없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        //when
        List<PostDto> postDtos = postService.getList(member.getId());

        //then
        assertThat(postDtos.isEmpty()).isTrue();
    }

    @Test
    void 일기_수정() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member.getId());

        PostUpdateDto postUpdateDto = new PostUpdateDto(postDto.getId(), "New Title", "New Content");

        //when
        PostDto finalPostDto = postService.update(postUpdateDto, member.getId());

        //then
        assertThat(postDto.getId()).isEqualTo(finalPostDto.getId());
        assertThat(finalPostDto.getMemberName()).isEqualTo(member.getName());
        assertThat(finalPostDto.getTitle()).isEqualTo("New Title");
        assertThat(finalPostDto.getBody()).isEqualTo("New Content");
    }

    @Test
    void 일기_수정_일기없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostUpdateDto postUpdateDto = new PostUpdateDto(100L, "New Title", "New Content");

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> postService.update(postUpdateDto, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 일기_수정_작성자아님() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);


        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member1.getId());

        PostUpdateDto postUpdateDto = new PostUpdateDto(postDto.getId(), "New Title", "New Content");

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> postService.update(postUpdateDto, member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 일기_삭제() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member.getId());

        //when
        boolean remove = postService.remove(member.getId(), postDto.getId());

        //then
        assertThat(remove).isTrue();
    }

    @Test
    void 일기_삭제_일기없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e= assertThrows(
                EmptyResultDataAccessException.class, () -> postService.remove(member.getId(), 100L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 일기_삭제_작성자아님() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member1.getId());

        //when
        AccessDeniedException e= assertThrows(
                AccessDeniedException.class, () -> postService.remove(member2.getId(), postDto.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 일기_존재_확인_일기있음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member.getId());

        //when
        Post getPost = postService.isExist(postDto.getId());

        assertThat(getPost.getId()).isEqualTo(postDto.getId());
        assertThat(getPost.getTitle()).isEqualTo("Title");
        assertThat(getPost.getBody()).isEqualTo("Content");
        assertThat(getPost.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    void 일기_존재_확인_일기없음() {
        //given

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> postService.isExist(1L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 일기_권한_확인_권한없음() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostCreateDto postCreateDto = new PostCreateDto("Title", "Content");
        PostDto postDto = postService.create(postCreateDto, member1.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> postService.isAuthor(member2.getId(), postDto.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }
}