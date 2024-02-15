package com.example.diary.controller;

import com.example.diary.domain.group.Group;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * 전체 그룹 조회
     */
    @GetMapping("")
    public List<GroupDto> getAll() {
        List<Group> allGroups = groupService.getAll();
        List<GroupDto> allGroupDtos = allGroups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return allGroupDtos;
    }


    /**
     * 단일 그룹 조회
     */
    @GetMapping("/{groupId}")
    public GroupDto get(@PathVariable @Valid Long groupId) {
        Group group = groupService.get(groupId);
        GroupDto groupDto = convertToDto(group);
        return groupDto;
    }

    /**
     * 그룹 생성
     */
    @PostMapping("/new")
    public Long create(@RequestBody @Valid GroupCreateDto groupCreateDto) {
        Long id = groupService.create(groupCreateDto);
        return id;
    }

    /**
     * 그룹 삭제
     */
    @DeleteMapping("/{groupId}/remove")
    public Long remove(@PathVariable @Valid Long groupId) {
        groupService.remove(groupId);
        return groupId;
    }

    private GroupDto convertToDto(Group group) {
        GroupDto groupDto = new GroupDto(
                group.getId(),
                group.getName(),
                group.getCreatedDate(),
                group.getGroupMembers()
        );
        return groupDto;
    }
}
