package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupCreateDto;
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
public class GroupService {

    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;

    private final GroupMemberService groupMemberService;
    private final GroupMemberRepository groupMemberRepository;

    /**
     * 내 그룹 목록
     */
    public List<GroupDto> getMyGroups(Long memberId) {

        List<GroupMember> groupMembers = groupMemberRepository.findByMemberId(memberId);
        List<Group> groups = groupMembers.stream()
                .filter(groupMember -> groupMember.getStatus())
                .map(groupMember -> groupRepository.findById(groupMember.getGroup().getId()).get())
                .collect(Collectors.toList());
        List<GroupDto> groupDtos = groups.stream()
                .map(group -> new GroupDto(group))
                .collect(Collectors.toList());

        return groupDtos;
    }

    /**
     * 단일 그룹 조회
     */
    public GroupDto get(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);

        isGroupMember(groupId, memberId);

        GroupDto groupDto = new GroupDto(group);

        return groupDto;
    }

    /**
     * 그룹 생성
     */
    @Transactional
    public GroupDto create(GroupCreateDto groupCreateDto, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        groupNameExist(groupCreateDto.getName());

        Group group = new Group(groupCreateDto, member);
        groupRepository.save(group);

        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(group.getName());
        groupMemberService.join(groupMemberCreateDto, memberId, true);

        GroupDto groupDto = new GroupDto(group);

        return groupDto;
    }

    /**
     * 그룹 삭제
     */
    @Transactional
    public boolean remove(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);
        isGroupLeader(groupId, memberId);

        groupRepository.deleteById(groupId);

        return true;
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
     * 그룹 이름 중복 확인
     */
    public void groupNameExist(String groupName) {

        Optional<Group> groupOptional = groupRepository.findByName(groupName);

        if (!groupOptional.isEmpty()) {
            throw new IllegalArgumentException("이미 존재하는 그룹 이름입니다.");
        }
    }

    /**
     * 그룹 리더 확인
     */
    public void isGroupLeader(Long groupId, Long memberId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Group group = groupOptional.get();

        if (group.getGroupLeader().getId() != memberId) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
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