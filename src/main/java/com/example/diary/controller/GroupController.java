package com.example.diary.controller;

import com.example.diary.domain.group.Group;
import com.example.diary.dto.group.GroupCreateDto;
import com.example.diary.dto.group.GroupDto;
import com.example.diary.service.GroupService;
import com.example.diary.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//    @GetMapping("")
//    public ResponseEntity<List<GroupDto>> getAll() {
//        List<GroupDto> groupDtos = groupService.getAll();
//        return new ResponseEntity<>(groupDtos, HttpStatus.OK);
//    }

    /**
     * 단일 그룹 조회
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> get(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        GroupDto groupDto = groupService.get(groupId, memberId);
        return new ResponseEntity<>(groupDto, HttpStatus.OK);
    }

    /**
     * 그룹 생성
     */
    @PostMapping("/new")
    public ResponseEntity<GroupDto> create(
            @RequestBody @Valid GroupCreateDto groupCreateDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        GroupDto groupDto = groupService.create(groupCreateDto, memberId);
        return new ResponseEntity<>(groupDto, HttpStatus.CREATED);
    }

    /**
     * 그룹 삭제
     */
    @DeleteMapping("/{groupId}/remove")
    public ResponseEntity remove(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        groupService.remove(groupId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
