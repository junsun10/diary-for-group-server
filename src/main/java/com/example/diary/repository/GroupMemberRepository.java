package com.example.diary.repository;

import com.example.diary.domain.group.Group;
import com.example.diary.domain.group.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    void deleteByGroupIdAndMemberId(Long groupId, Long memberId);

    List<GroupMember> findByMemberId(Long memberId);
}
