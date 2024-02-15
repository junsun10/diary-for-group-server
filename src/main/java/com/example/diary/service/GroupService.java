package com.example.diary.service;

import com.example.diary.domain.group.Group;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * 그룹 생성
     */
    @Transactional
    public Long create(GroupCreateDto groupCreateDto) {
        Group group = new Group(groupCreateDto.getName());
        groupRepository.save(group);
        return group.getId();
    }

    /**
     * 단일 그룹 조회
     */
    public Group get(Long groupId) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        Group group = groupOptional.get();
        return group;
    }

    /**
     * 전체 그룹 조회
     */
    public List<Group> getAll() {
        List<Group> allGroups = groupRepository.findAll();
        return allGroups;
    }

    /**
     * 그룹 삭제
     */
    @Transactional
    public Long remove(Long groupId) {
        groupRepository.deleteById(groupId);
        return groupId;
    }
}
