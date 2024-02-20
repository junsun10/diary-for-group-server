package com.example.diary.controller;

import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.dto.group.GroupMemberDto;
import com.example.diary.service.GroupMemberService;
import com.example.diary.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/group-member")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    /**
     * 그룹 참여
     */
    @PostMapping("/new")
    public ResponseEntity<GroupMemberDto> join(
            @RequestBody @Valid GroupMemberCreateDto groupMemberCreateDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        GroupMemberDto groupMemberDto = groupMemberService.join(groupMemberCreateDto, memberId);
        return new ResponseEntity<>(groupMemberDto, HttpStatus.CREATED);
    }

    /**
     * 그룹 탈퇴
     */
    @DeleteMapping("/{groupId}/remove")
    public ResponseEntity out(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        groupMemberService.out(groupId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 내 참여 그룹 조회
     */
    @GetMapping("")
    public ResponseEntity<List<GroupDto>> myGroups(
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<GroupDto> myGroupDtos = groupMemberService.getMyGroupList(memberId);
        return new ResponseEntity<>(myGroupDtos, HttpStatus.OK);
    }
}
