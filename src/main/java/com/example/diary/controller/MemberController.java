package com.example.diary.controller;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.member.MemberCreateDto;
import com.example.diary.dto.member.MemberDto;
import com.example.diary.dto.member.MemberLoginDto;
import com.example.diary.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // TODO: 리턴 타입 정리

    /**
     * 회원 가입
     */
    @PostMapping("/new")
    public MemberDto create(@RequestBody @Valid MemberCreateDto memberCreateDto) {
        Member member = new Member(memberCreateDto.getName(), memberCreateDto.getPassword(), memberCreateDto.getEmail());
        memberService.join(member);

        log.info("create member");

        MemberDto memberDto = new MemberDto(member.getId(), member.getName());
        return memberDto;
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/remove/{id}")
    public Long remove(@PathVariable @Valid Long id) {
        memberService.remove(id);
        log.info("remove member");
        return id;
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public MemberDto login(@RequestBody @Valid MemberLoginDto memberLoginDto) {
        MemberDto memberDto = memberService.login(memberLoginDto);
        return memberDto;
    }
}
