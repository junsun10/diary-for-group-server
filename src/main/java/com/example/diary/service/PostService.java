package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostUpdateDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.GroupMemberRepository;
import com.example.diary.repository.GroupRepository;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final GroupRepository groupRepository;
    private final GroupMemberService groupMemberService;
    private final GroupMemberRepository groupMemberRepository;

    /**
     * 일기 생성
     */
    @Transactional
    public PostDto create(PostCreateDto postCreateDto, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        Group group = groupIsExist(postCreateDto.getGroupId());
        isGroupMember(group.getId(), memberId);

        Post post = new Post(member, group, postCreateDto);
        postRepository.save(post);
        PostDto postDto = new PostDto(post);

        log.info("create post");

        return postDto;
    }

    /**
     * 단일 일기 조회
     */
    @Transactional
    public PostDto get(Long memberId, Long postId) {

        Post post = isExist(postId);

        // 일기가 속한 그룹 멤버만 읽기 가능
        isGroupMember(post.getGroup().getId(), memberId);

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
     * 단일 그룹 일기 목록 조회
     */
    public List<PostDto> getGroupPostList(Long memberId, Long groupId) {

        Group group = groupIsExist(groupId);

        isGroupMember(groupId, memberId);

        List<Post> posts = postRepository.findByGroupId(group.getId());
        List<PostDto> postDtos = posts.stream()
                .map(post -> new PostDto(post))
                .collect(Collectors.toList());

        log.info("get group post list");

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

    /**
     * 그룹 존재 확인
     */
    public Group groupIsExist(Long groupId) {

        Optional<Group> groupOptional = groupRepository.findById(groupId);

        if (groupOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("존재하지 않는 그룹입니다.", 0);
        }

        Group group = groupOptional.get();

        return group;
    }

    /**
     * 그룹 멤버 확인
     */
    public void isGroupMember(Long groupId, Long memberId) {
        Optional<GroupMember> groupMemberOptional = groupMemberRepository.findByMemberIdAndGroupId(memberId, groupId);

        if (groupMemberOptional.isEmpty()) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}
