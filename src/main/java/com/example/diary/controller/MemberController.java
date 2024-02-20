package com.example.diary.controller;

import com.example.diary.dto.member.MemberCreateDto;
import com.example.diary.dto.member.MemberDto;
import com.example.diary.dto.member.MemberLoginDto;
import com.example.diary.service.MemberService;
import com.example.diary.session.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @PostMapping("/new")
    public ResponseEntity<MemberDto> create(
            @RequestBody @Valid MemberCreateDto memberCreateDto) {
        MemberDto memberDto = memberService.join(memberCreateDto);
        return new ResponseEntity<>(memberDto, HttpStatus.CREATED);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Long> remove(
            HttpServletRequest request) {
        Long memberId = SessionUtils.getMemberIdFromSession(request);
        memberService.remove(memberId);
        return new ResponseEntity<>(memberId, HttpStatus.OK);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<MemberDto> login(
            @RequestBody @Valid MemberLoginDto memberLoginDto, HttpServletRequest request) {
        MemberDto memberDto = memberService.login(memberLoginDto, request);
        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        memberService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
