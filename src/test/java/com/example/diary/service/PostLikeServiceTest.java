package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.member.Member;
import com.example.diary.domain.post.Post;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.post.PostCreateDto;
import com.example.diary.dto.post.PostDto;
import com.example.diary.dto.post.PostLikeCreateDto;
import com.example.diary.dto.post.PostLikeDto;
import com.example.diary.repository.GroupRepository;
import com.example.diary.repository.MemberRepository;
import com.example.diary.repository.PostLikeRepository;
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
class PostLikeServiceTest {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    void 좋아요_등록() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(post.getId());

        //when
        PostLikeDto postLikeDto = postLikeService.add(postLikeCreateDto, member2.getId());

        //then
        assertThat(postLikeDto.getPostId()).isEqualTo(post.getId());
        assertThat(postLikeDto.getMemberName()).isEqualTo(member2.getName());
    }

    @Test
    void 좋아요_등록_일기없음() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(0L);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> postLikeService.add(postLikeCreateDto, member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 좋아요_등록_중복() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(post.getId());
        PostLikeDto postLikeDto1 = postLikeService.add(postLikeCreateDto, member2.getId());

        //when
        IllegalStateException e = assertThrows(
                IllegalStateException.class, () -> postLikeService.add(postLikeCreateDto, member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 좋아요를 눌렀습니다.");
    }

    @Test
    void 좋아요_취소() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(post.getId());
        PostLikeDto postLikeDto = postLikeService.add(postLikeCreateDto, member2.getId());

        //when
        boolean remove = postLikeService.remove(member2.getId(), post.getId());

        //then
        assertThat(remove).isTrue();
    }

    @Test
    void 좋아요_취소_좋아요없음() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> postLikeService.remove(member2.getId(), post.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 좋아요입니다.");
    }

    @Test
    void 단일_일기_좋아요_목록() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(post.getId());
        PostLikeDto postLikeDto1 = postLikeService.add(postLikeCreateDto, member1.getId());
        PostLikeDto postLikeDto2 = postLikeService.add(postLikeCreateDto, member2.getId());

        //when
        List<PostLikeDto> postLikeDtos = postLikeService.list(post.getId());

        //then
        assertThat(postLikeDtos.size()).isEqualTo(2);
    }

    @Test
    void 단일_일기_좋아요_목록_좋아요없음() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        //when
        List<PostLikeDto> postLikeDtos = postLikeService.list(post.getId());

        //then
        assertThat(postLikeDtos.isEmpty()).isTrue();
    }

    @Test
    void 단일_일기_좋아요_목록_일기없음() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> postLikeService.list(0L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }

    @Test
    void 단일_일기_좋아요_등록_여부_좋아요누름() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        PostLikeCreateDto postLikeCreateDto = new PostLikeCreateDto(post.getId());
        PostLikeDto postLikeDto = postLikeService.add(postLikeCreateDto, member2.getId());

        //when
        boolean status = postLikeService.status(member2.getId(), post.getId());

        //then
        assertThat(status).isTrue();
    }

    @Test
    void 단일_일기_좋아요_등록_여부_좋아요안누름() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());
        Optional<Group> groupOptional = groupRepository.findById(groupDto.getId());
        Group group = groupOptional.get();

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member1, group, postCreateDto);
        postRepository.save(post);

        //when
        boolean status = postLikeService.status(member2.getId(), post.getId());

        //then
        assertThat(status).isFalse();
    }

    @Test
    void 일기_존재_확인_일기있음() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());
        Group group = new Group(groupCreateDto, member);

        PostCreateDto postCreateDto = new PostCreateDto(group.getId(), "Title", "Content");
        Post post = new Post(member, group, postCreateDto);
        postRepository.save(post);

        //when
        Post getPost = postLikeService.postIsExist(post.getId());

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
                EmptyResultDataAccessException.class, () -> postLikeService.postIsExist(0L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 일기입니다.");
    }
}