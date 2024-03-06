package com.example.diary.controller;

import com.example.diary.dto.group.GroupDto;
import com.example.diary.dto.group.GroupMemberCreateDto;
import com.example.diary.dto.group.GroupMemberDto;
import com.example.diary.dto.group.GroupMemberJoinDto;
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
        GroupMemberDto groupMemberDto = groupMemberService.join(groupMemberCreateDto, memberId, false);
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
     * 그룹원 목록
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupMemberDto>> members(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long leaderId = SessionUtils.getMemberIdFromSession(request);
        List<GroupMemberDto> groupMemberDtoList = groupMemberService.members(groupId, leaderId);
        return new ResponseEntity<>(groupMemberDtoList, HttpStatus.OK);
    }

    /**
     * 그룹 추방
     */
    @DeleteMapping("/{groupId}/{memberId}/remove")
    public ResponseEntity remove(
            @PathVariable @Valid Long groupId, @PathVariable @Valid Long memberId, HttpServletRequest request) {
        Long leaderId = SessionUtils.getMemberIdFromSession(request);
        groupMemberService.outByLeader(groupId, memberId, leaderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 내 참여 그룹 조회
     */
//    @GetMapping("")
//    public ResponseEntity<List<GroupDto>> myGroups(
//            HttpServletRequest request) {
//        Long memberId = SessionUtils.getMemberIdFromSession(request);
//        List<GroupDto> myGroupDtos = groupMemberService.getMyGroupList(memberId);
//        return new ResponseEntity<>(myGroupDtos, HttpStatus.OK);
//    }

    /**
     * 그룹 가입 승인
     */
    @PostMapping("/group/accept")
    public ResponseEntity<GroupMemberDto> joinAccept(
            @RequestBody @Valid GroupMemberJoinDto groupMemberJoinDtoDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        GroupMemberDto groupMemberDto = groupMemberService.joinAccept(groupMemberJoinDtoDto, memberId);
        return new ResponseEntity<>(groupMemberDto, HttpStatus.OK);
    }

    /**
     * 그룹 가입 거절
     */
    @PostMapping("/group/reject")
    public ResponseEntity joinReject(
            @RequestBody @Valid GroupMemberJoinDto groupMemberJoinDtoDto, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        groupMemberService.joinReject(groupMemberJoinDtoDto, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 그룹 가입 요청 취소
     */
    @DeleteMapping("/{groupId}/cancel")
    public ResponseEntity cancel(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        groupMemberService.out(groupId, memberId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 그룹 가입 신청 목록
     */
    @GetMapping("/request/{groupId}")
    public ResponseEntity<List<GroupMemberDto>> groupJoinRequestList(
            @PathVariable @Valid Long groupId, HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<GroupMemberDto> groupMemberDtos = groupMemberService.joinRequestList(groupId, memberId);
        return new ResponseEntity<>(groupMemberDtos, HttpStatus.OK);
    }

    /**
     * 내 가입 신청 목록
     */
    @GetMapping("/my-request")
    public ResponseEntity<List<GroupMemberDto>> myJoinRequestList(
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        List<GroupMemberDto> groupMemberDtos = groupMemberService.myJoinRequestList(memberId);
        return new ResponseEntity<>(groupMemberDtos, HttpStatus.OK);
    }
}
