package com.example.diary.controller;

import com.example.diary.domain.group.Group;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.service.GroupMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/member")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    /**
     * 그룹 참여
     */
    @PostMapping("/new")
    public Long join(@RequestBody @Valid GroupMemberCreateDto groupMemberCreateDto) {
        Long id = groupMemberService.join(groupMemberCreateDto);
        return id;
    }

    /**
     * 그룹 탈퇴
     */
    @DeleteMapping("/{groupId}/{memberId}/remove")
    public void out(@PathVariable @Valid Long groupId, @PathVariable @Valid Long memberId) {
        groupMemberService.out(groupId, memberId);
    }

    /**
     * 내 참여 그룹 조회
     */
    @GetMapping("/{memberId}")
    public List<GroupDto> myGroups(@PathVariable @Valid Long memberId) {
        List<Group> myGroups = groupMemberService.getMyGroupList(memberId);
        List<GroupDto> myGroupDtos = myGroups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return myGroupDtos;
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
