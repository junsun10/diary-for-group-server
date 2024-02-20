package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
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

    /**
     * 전체 그룹 조회
     */
//    public List<GroupDto> getAll() {
//        List<Group> groups = groupRepository.findAll();
//        List<GroupDto> groupDtos = groups.stream()
//                .map(group -> new GroupDto(group))
//                .collect(Collectors.toList());
//
//        return groupDtos;
//    }

    /**
     * 단일 그룹 조회
     */
    public GroupDto get(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);

        isGroupMember(group, memberId);

        GroupDto groupDto = new GroupDto(group);

        return groupDto;
    }

    /**
     * 그룹 생성
     */
    @Transactional
    public GroupDto create(GroupCreateDto groupCreateDto, Long memberId) {
        Group group = new Group(groupCreateDto);
        groupRepository.save(group);

        GroupMemberCreateDto groupMemberCreateDto = new GroupMemberCreateDto(group.getId());
        groupMemberService.join(groupMemberCreateDto, memberId);

        GroupDto groupDto = new GroupDto(group);

        return groupDto;
    }

    /**
     * 그룹 삭제
     */
    @Transactional
    public boolean remove(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);
        isGroupMember(group, memberId);

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
     * 그룹 멤버 확인
     */
    public void isGroupMember(Group group, Long memberId) {
        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        if (!group.getGroupMembers().contains(member)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}