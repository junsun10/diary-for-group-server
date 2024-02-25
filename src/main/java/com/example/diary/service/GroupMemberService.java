package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.dto.group.GroupMemberDto;
import com.example.diary.dto.group.GroupMemberJoinDto;
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
    public GroupMemberDto join(GroupMemberCreateDto groupMemberCreateDto, Long memberId, boolean leader) {

        Optional<Member> memberOptional = memberRepository.findById(memberId);
        Member member = memberOptional.get();

        Group group = groupIsExistByName(groupMemberCreateDto.getGroupName());
        isGroupMember(group.getId(), memberId);

        GroupMember groupMember = new GroupMember(group, member, leader);
        groupMemberRepository.save(groupMember);
        GroupMemberDto groupMemberDto = new GroupMemberDto(groupMember);

        return groupMemberDto;
    }

    /**
     * 그룹원 목록
     */
    public List<GroupMemberDto> members(Long groupId, Long leaderId) {

        Group group = groupIsExist(groupId);
        isNotGroupMember(group.getId(), leaderId);
        isGroupLeader(group, leaderId);

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);
        List<GroupMemberDto> groupMemberDtoList = groupMembers.stream()
                .filter(member -> member.getStatus())
                .map(member -> new GroupMemberDto(member))
                .collect(Collectors.toList());
        return groupMemberDtoList;
    }

    /**
     * 그룹 탈퇴
     */
    @Transactional
    public boolean out(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);

        isNotGroupMember(groupId, memberId);

        groupMemberRepository.deleteByGroupIdAndMemberId(groupId, memberId);

        return true;
    }

    /**
     * 그룹 추방
     */
    @Transactional
    public boolean outByLeader(Long groupId, Long memberId, Long leaderId) {

        Group group = groupIsExist(groupId);

        isNotGroupMember(groupId, memberId);

        isGroupLeader(group, leaderId);

        groupMemberRepository.deleteByGroupIdAndMemberId(groupId, memberId);

        return true;
    }

    /**
     * 내 참여 그룹 조회
     */
//    public List<GroupDto> getMyGroupList(Long memberId) {
//        List<GroupMember> myGroupMembers = groupMemberRepository.findByMemberId(memberId);
//        List<Group> myGroups = myGroupMembers.stream()
//                .filter(groupMember -> groupMember.getStatus())
//                .map(GroupMember::getGroup)
//                .collect(Collectors.toList());
//        List<GroupDto> myGroupDtos = myGroups.stream()
//                .map(group -> new GroupDto(group))
//                .collect(Collectors.toList());
//
//        log.info("size: " + Integer.toString(myGroupDtos.size()));
//        return myGroupDtos;
//    }

    /**
     * 그룹 가입 승인
     */
    @Transactional
    public GroupMemberDto joinAccept(GroupMemberJoinDto groupMemberJoinDto, Long leaderId) {

        Group group = groupIsExist(groupMemberJoinDto.getGroupId());

        isGroupMember(group.getId(), groupMemberJoinDto.getMemberId());

        isGroupLeader(group, leaderId);

        Optional<GroupMember> groupMemberOptional =
                groupMemberRepository.findByMemberIdAndGroupId(groupMemberJoinDto.getMemberId(), group.getId());
        GroupMember groupMember = groupMemberOptional.get();

        groupMember.accept();

        GroupMemberDto groupMemberDto = new GroupMemberDto(groupMember);

        return groupMemberDto;
    }

    /**
     * 그룹 가입 거절
     */
    @Transactional
    public boolean joinReject(GroupMemberJoinDto groupMemberJoinDto, Long leaderId) {

        Group group = groupIsExist(groupMemberJoinDto.getGroupId());

        isGroupMember(group.getId(), groupMemberJoinDto.getMemberId());

        isGroupLeader(group, leaderId);

        Optional<GroupMember> groupMemberOptional =
                groupMemberRepository.findByMemberIdAndGroupId(groupMemberJoinDto.getMemberId(), group.getId());
        GroupMember groupMember = groupMemberOptional.get();

        groupMemberRepository.deleteById(groupMember.getId());

        log.info("reject request");

        return true;
    }

    /**
     * 그룹 가입 신청 목록
     */
    public List<GroupMemberDto> joinRequestList(Long groupId, Long memberId) {

        Group group = groupIsExist(groupId);

        isGroupLeader(group, memberId);

        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);
        List<GroupMemberDto> groupMemberDtos = groupMembers.stream()
                .filter(groupMember -> groupMember.getStatus()==false)
                .map(groupMember -> new GroupMemberDto(groupMember))
                .collect(Collectors.toList());

        return groupMemberDtos;
    }

    /**
     * 내 가입 신청 목록
     */
    public List<GroupMemberDto> myJoinRequestList(Long memberId) {

        List<GroupMember> groupMembers = groupMemberRepository.findByMemberId(memberId);
        List<GroupMemberDto> groupMemberDtos = groupMembers.stream()
                .map(groupMember -> new GroupMemberDto(groupMember))
                .collect(Collectors.toList());

        return groupMemberDtos;
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
     * 그룹 존재 확인
     */
    public Group groupIsExistByName(String groupName) {

        Optional<Group> groupOptional = groupRepository.findByName(groupName);

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

        if (!groupMemberOptional.isEmpty()) {
            GroupMember groupMember = groupMemberOptional.get();
            if (groupMember.getStatus()) {
                throw new IllegalStateException("이미 가입한 그룹입니다.");
            }
        }
    }

    /**
     * 그룹 멤버 확인
     */
    public void isNotGroupMember(Long groupId, Long memberId) {
        Optional<GroupMember> groupMemberOptional = groupMemberRepository.findByMemberIdAndGroupId(memberId, groupId);

        if (groupMemberOptional.isEmpty()) {
            throw new EmptyResultDataAccessException("가입하지 않은 그룹입니다.", 0);
        }
        else {
            GroupMember groupMember = groupMemberOptional.get();
            if (groupMember.getStatus() == false) {
                throw new EmptyResultDataAccessException("가입 요청이 승인되지 않은 그룹입니다.", 0);
            }
        }
    }

    /**
     * 그룹 리더 확인
     */
    public void isGroupLeader(Group group, Long memberId) {

        if (group.getGroupLeader().getId() != memberId) {
            throw new AccessDeniedException("그룹 리더가 아닙니다.");
        }
    }
}
