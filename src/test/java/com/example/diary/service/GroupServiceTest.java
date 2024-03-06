package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.GroupMemberRepository;
import com.example.diary.repository.GroupRepository;
import com.example.diary.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupMemberService groupMemberService;

    @Test
    void 단일_그룹_조회() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        GroupDto findGroupDto = groupService.get(groupDto.getId(), member.getId());

        //then
        assertThat(findGroupDto.getName()).isEqualTo("group");
    }

    @Test
    void 단일_그룹_조회_그룹없음() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupService.get(0L, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 단일_그룹_조회_멤버아님() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> groupService.get(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 그룹_생성() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        //when
        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //then
        assertThat(groupDto.getName()).isEqualTo(groupCreateDto.getName());
        assertThat(groupDto.getGroupLeaderId()).isEqualTo(member.getId());
    }

    @Test
    void 그룹_삭제() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        boolean remove = groupService.remove(groupDto.getId(), member.getId());

        //then
        assertThat(remove).isTrue();
    }

    @Test
    void 그룹_삭제_그룹없음() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupService.remove(0L, member.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 그룹_삭제_권한없음() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> groupService.remove(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 그룹_존재_확인_그룹있음() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member.getId());

        //when
        Group group = groupService.groupIsExist(groupDto.getId());

        //then
        assertThat(group.getName()).isEqualTo("group");
        assertThat(group.getGroupLeader().getId()).isEqualTo(member.getId());
    }

    @Test
    void 그룹_존재_확인_그룹없음() {
        //given
        Member member = new Member("username", "testpassword", "user@test.com");
        memberRepository.save(member);

        //when
        EmptyResultDataAccessException e = assertThrows(
                EmptyResultDataAccessException.class, () -> groupService.groupIsExist(0L));

        //then
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 그룹입니다.");
    }

    @Test
    void 그룹_리더_확인_리더아님() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> groupService.isGroupLeader(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }

    @Test
    void 그룹_멤버_확인_멤버아님() {
        //given
        Member member1 = new Member("username1", "testpassword", "user1@test.com");
        Member member2 = new Member("username2", "testpassword", "user2@test.com");
        memberRepository.save(member1);
        memberRepository.save(member2);

        GroupCreateDto groupCreateDto = new GroupCreateDto("group");
        GroupDto groupDto = groupService.create(groupCreateDto, member1.getId());

        //when
        AccessDeniedException e = assertThrows(
                AccessDeniedException.class, () -> groupService.isGroupMember(groupDto.getId(), member2.getId()));

        //then
        assertThat(e.getMessage()).isEqualTo("권한이 없습니다.");
    }
}