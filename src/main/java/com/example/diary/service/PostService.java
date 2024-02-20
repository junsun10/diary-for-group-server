package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostUpdateDto;
import com.example.diary.exception.AccessDeniedException;
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
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // TODO: findby 존재하지 않는 경우 처리

    /**
     * 일기 생성
     */
    @Transactional
    public PostDto create(PostCreateDto postCreateDto, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        Post post = new Post(member, postCreateDto);
        postRepository.save(post);
        PostDto postDto = new PostDto(post);

        log.info("create post");

        return postDto;
    }

    //TODO: 댓글 목록
    /**
     * 단일 일기 조회
     */
    @Transactional
    public PostDto get(Long memberId, Long postId) {

        Post post = isExist(postId);
        isAuthor(memberId, postId);

        post.addView(); //조회수 증가

        PostDto postDto = new PostDto(post);

        log.info("get post");

        return postDto;
    }

    /**
     * 내 일기 목록 조회
     */
    public List<PostDto> getList(Long memberId) {

        List<Post> posts = postRepository.findByMemberId(memberId);
        List<PostDto> postDtos = posts.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());

        log.info("get member post list");

        return postDtos;
    }

    /**
     * 일기 수정
     */
    @Transactional
    public PostDto update(PostUpdateDto postUpdateDto, Long memberId) {

        Post post = isExist(postUpdateDto.getId());
        isAuthor(memberId, post.getId());

        post.update(postUpdateDto);

        PostDto postDto = new PostDto(post);

        log.info("update post");

        return postDto;
    }

    /**
     * 일기 삭제
     */
    @Transactional
    public boolean remove(Long memberId, Long postId) {

        Post post = isExist(postId);
        isAuthor(memberId, post.getId());

        postRepository.deleteById(postId);

        log.info("remove post");

        return true;
    }

    /**
     * 일기 존재 확인
     */
    public Post isExist(Long postId) {

        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 일기입니다.", 0);
        }

        Post post = postOptional.get();

        return post;
    }

    /**
     * 일기 권한 확인
     */
    public void isAuthor(Long memberId, Long postId) {

        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.get();

        if (post.getMember().getId() != memberId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}
