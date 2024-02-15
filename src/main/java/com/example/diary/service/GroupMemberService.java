package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import com.example.diary.domain.member.Member;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.repository.GroupMemberRepository;
import com.example.diary.repository.GroupRepository;
import com.example.diary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Long join(GroupMemberCreateDto groupMemberCreateDto) {
        Optional<Group> groupOptional = groupRepository.findById(groupMemberCreateDto.getGroupId());
        Group group = groupOptional.get();

        Optional<Member> memberOptional = memberRepository.findById(groupMemberCreateDto.getMemberId());
        Member member = memberOptional.get();

        GroupMember groupMember = new GroupMember(group, member);
        groupMemberRepository.save(groupMember);
        return group.getId();
    }

    /**
     * 그룹 탈퇴
     */
    @Transactional
    public void out(Long groupId, Long memberId) {
        groupMemberRepository.deleteByGroupIdAndMemberId(groupId, memberId);
    }

    /**
     * 내 참여 그룹 조회
     */
    public List<Group> getMyGroupList(Long memberId) {
        List<GroupMember> myGroupMemberList = groupMemberRepository.findByMemberId(memberId);
        List<Group> myGroupList = myGroupMemberList.stream()
                .map(GroupMember::getGroup)
                .collect(Collectors.toList());
        return myGroupList;
    }
}
