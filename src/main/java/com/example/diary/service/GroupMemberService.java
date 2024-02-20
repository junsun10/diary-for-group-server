package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.dto.group.GroupMemberDto;
import com.example.diary.exception.AccessDeniedException;
import com.example.diary.repository.GroupMemberRepository;
import com.example.diary.repository.GroupRepository;
import com.example.diary.repository.MemberRepository;
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
public class GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;

    /**
     * 그룹 참여
     */
    @Transactional
    public GroupMemberDto join(GroupMemberCreateDto groupMemberCreateDto, Long memberId) {


        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        Group group = groupIsExist(groupMemberCreateDto.getGroupId());
        isGroupMember(group, memberId);

        GroupMember groupMember = new GroupMember(group, member);
        groupMemberRepository.save(groupMember);
        GroupMemberDto groupMemberDto = new GroupMemberDto(groupMember);

        return groupMemberDto;
    }

    /**
     * 그룹 탈퇴
     */
    @Transactional
    public boolean out(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);

        groupMemberRepository.deleteByGroupIdAndMemberId(groupId, memberId);

        return true;
    }

    /**
     * 내 참여 그룹 조회
     */
    public List<GroupDto> getMyGroupList(Long memberId) {

        List<GroupMember> myGroupMembers = groupMemberRepository.findByMemberId(memberId);
        List<Group> myGroups = myGroupMembers.stream()
                .map(GroupMember::getGroup)
                .collect(Collectors.toList());
        List<GroupDto> myGroupDtos = myGroups.stream()
                .map(group -> new GroupDto(group))
                .collect(Collectors.toList());

        return myGroupDtos;
    }

    /**
     * 그룹 존재 확인
     */
    private Group groupIsExist(Long groupId) {

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
    public void isGroupMember(Group group, Long memberId) {

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        if (group.getGroupMembers().contains(member.getId())) {
            throw new IllegalStateException("이미 그룹에 존재하는 회원입니다.");
        }
    }
}
