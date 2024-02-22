package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.dto.group.GroupMemberDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.MemberRepository;
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
class GroupMemberServiceTest {

    @Autowired
    private GroupMemberService groupMemberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupService groupService;


    @Test
    void 그룹_참여() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(groupDto.getId());
        GroupMemberDto groupMemberDto = groupMemberService.join(groupMemberCreateDto, member2.getId());

        //then
        assertThat(groupMemberDto.getMemberId()).isEqualTo(member2.getId());
        assertThat(groupMemberDto.getGroupId()).isEqualTo(groupDto.getId());
    }

    @Test
    void 그룹_참여_그룹없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        //when
        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(100L);
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupMemberService.join(groupMemberCreateDto, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 그룹_참여_가입한그룹() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(groupDto.getId());
        IllegalStateException e = assertThrows(
                IllegalStateException.class, () -> groupMemberService.join(groupMemberCreateDto, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 가입한 그룹입니다.");
    }

    @Test
    void 그룹_탈퇴() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(groupDto.getId());
        GroupMemberDto groupMemberDto = groupMemberService.join(groupMemberCreateDto, member2.getId());

        //when
        boolean out = groupMemberService.out(groupDto.getId(), member2.getId());

        //then
        assertThat(out).isTrue();
    }

    @Test
    void 그룹_탈퇴_그룹없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupMemberService.out(100L, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 그룹_탈퇴_멤버아님() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupMemberService.out(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("가입하지 않은 그룹입니다.");
    }

    @Test
    void 소속_그룹_목록() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto1 = new GroupCreateDto("group1");
        GroupCreateDto groupCreateDto2 = new GroupCreateDto("group2");
        GroupDto groupDto1 = groupService.create(groupCreateDto1, member1.getId());
        GroupDto groupDto2 = groupService.create(groupCreateDto2, member2.getId());

        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(groupDto2.getId());
        GroupMemberDto groupMemberDto = groupMemberService.join(groupMemberCreateDto, member1.getId());

        //when
        List<GroupDto> myGroupList = groupMemberService.getMyGroupList(member1.getId());

        //then
        assertThat(myGroupList.size()).isEqualTo(2);
    }

    @Test
    void 그룹_존재_확인_그룹있음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        Group group = groupMemberService.groupIsExist(groupDto.getId());

        //then
        assertThat(group.getName()).isEqualTo("group");
        assertThat(group.getGroupLeader().getId()).isEqualTo(member.getId());
    }

    @Test
    void 그룹_존재_확인_그룹없음() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupMemberService.groupIsExist(100L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 그룹_멤버_확인_멤버() {
        //given
        Member member = new Member("username", "12345!", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        IllegalStateException e = assertThrows(
                IllegalStateException.class, () -> groupMemberService.isGroupMember(groupDto.getId(), member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo(("이미 가입한 그룹입니다."));
    }

    @Test
    void 그룹_멤버_확인_멤버아님() {
        //given
        Member member1 = new Member("username1", "12345!", "user1@test.com");
        Member member2 = new Member("username2", "12345!", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupMemberService.isNotGroupMember(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo(("가입하지 않은 그룹입니다."));
    }
}