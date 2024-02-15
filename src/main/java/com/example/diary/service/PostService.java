package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostUpdateDto;
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
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    // TODO: findby 존재하지 않는 경우 처리

    /**
     * 일기 생성
     */
    @Transactional
    public Long create(PostCreateDto postCreateDto) {
        Optional<Member> memberOptional = memberRepository.findById(postCreateDto.getMemberId());
        Member member = memberOptional.get();
        Post post = new Post(
                member,
                postCreateDto.getTitle(),
                postCreateDto.getBody());
        postRepository.save(post);
        log.info("create post");
        return post.getId();
    }

    //TODO: 댓글 목록
    /**
     * 단일 일기 조회
     */
    @Transactional
    public Post get(Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Post post = postOptional.get();
        post.addView();
        log.info("get post");
        return post;
    }

    /**
     * 내 일기 목록 조회
     */
    public List<Post> getList(Long memberId) {
        List<Post> posts = postRepository.findByMemberId(memberId);
        return posts;
    }

    /**
     * 일기 수정
     */
    @Transactional
    public Long update(PostUpdateDto postUpdateDto) {
        Optional<Post> postOptional = postRepository.findById(postUpdateDto.getId());
        Post post = postOptional.get();
        post.update(postUpdateDto);
        return post.getId();
    }

    // TODO: 좋아요 정리
    /**
     * 일기 삭제
     */
    @Transactional
    public Long remove(Long id) {
        postRepository.deleteById(id);
        log.info("remove post");
        return id;
    }
}
