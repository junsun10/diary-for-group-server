package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.domain.post.PostLike;
import com.example.diary.dto.post.PostLikeDto;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostLikeRepository;
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
public class PostLikeService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    /**
     * 좋아요 등록
     */
    @Transactional
    public Long add(PostLikeDto postLikeDto, Long memberId) {

        Post post = postIsExist(postLikeDto.getPostId());

        if (postLikeRepository.existsByMemberIdAndPostId(memberId, postLikeDto.getPostId())) {
            throw new IllegalStateException("이미 좋아요를 눌렀습니다.");
        }

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        PostLike postLike = new PostLike(member, post);
        postLikeRepository.save(postLike);

        log.info("add post like");

        return postLike.getId();
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public boolean remove(Long memberId, Long postId) {

        postIsExist(postId);

        Optional<PostLike> postLikeOptional = postLikeRepository.findByMemberIdAndPostId(memberId, postId);
        if (postLikeOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 좋아요입니다.", 0);
        }
        PostLike postLike = postLikeOptional.get();

        postLikeRepository.delete(postLike);

        log.info("remove post like");

        return true;
    }

    /**
     * 좋아요 목록
     */
    public List<PostLikeDto> list(Long postId) {

        postIsExist(postId);

        Optional<List<PostLike>> postLikesOptional = postLikeRepository.findByPostId(postId);
        List<PostLike> postLikes = postLikesOptional.get();
        List<PostLikeDto> postLikeDtos = postLikes.stream()
                .map(postLike -> new PostLikeDto(postLike))
                .collect(Collectors.toList());

        return postLikeDtos;
    }

    /**
     * 단일 일기에 대한 나의 좋아요 등록 여부
     */
    public boolean status(Long memberId, Long postId) {

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        if (memberOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 회원입니다.", 0);
        }

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 일기입니다.", 0);
        }

        Optional<PostLike> postLikeOptional = postLikeRepository.findByMemberIdAndPostId(memberId, postId);
        if (postLikeOptional.isEmpty()) {
            return false;
        }
        else {
            return true;
        }
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
}
