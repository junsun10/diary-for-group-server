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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public Long add(PostLikeDto postLikeDto) {
        Optional<Member> memberOptional = memberRepository.findById(postLikeDto.getMemberId());
        Member member = memberOptional.get();

        Optional<Post> postOptional = postRepository.findById(postLikeDto.getPostId());
        Post post = postOptional.get();

        PostLike postLike = new PostLike(member, post);
        postLikeRepository.save(postLike);

        return postLike.getId();
    }

    /**
     * 좋아요 취소
     */
    @Transactional
    public Long remove(Long memberId, Long postId) {
        PostLike find = postLikeRepository.findByMemberIdAndPostId(memberId, postId);
        postLikeRepository.delete(find);
        return find.getId();
    }
}
