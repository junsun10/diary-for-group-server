package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.member.MemberCreateDto;
import com.example.diary.dto.member.MemberDto;
import com.example.diary.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 회원가입()  {
        //given
        MemberCreateDto memberCreateDto = new MemberCreateDto("username", "testpassword", "user@test.com");

        //when
        MemberDto memberDto = memberService.join(memberCreateDto);

        //then
        assertThat(memberDto.getName()).isEqualTo("username");
    }

    @Test
    void 회원가입_예외_중복_이름()  {
        //given
        MemberCreateDto memberCreateDto1 = new MemberCreateDto("username1", "testpassword", "user@test.com");
        MemberCreateDto memberCreateDto2 = new MemberCreateDto("username1", "testpassword", "user2@test.com");

        //when
        MemberDto memberDto1 = memberService.join(memberCreateDto1);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> memberService.join(memberCreateDto2));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 아이디입니다.");
    }

    @Test
    void 회원가입_예외_중복_이메일()  {
        //given
        MemberCreateDto memberCreateDto1 = new MemberCreateDto("username1", "testpassword", "user@test.com");
        MemberCreateDto memberCreateDto2 = new MemberCreateDto("username2", "testpassword", "user@test.com");

        //when
        MemberDto memberDto1 = memberService.join(memberCreateDto1);

        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> memberService.join(memberCreateDto2));

        //then
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
    }

    @Test
    void 회원탈퇴() {
        //given
        MemberCreateDto memberCreateDto = new MemberCreateDto("username", "testpassword", "user@test.com");
        memberService.join(memberCreateDto);

        Optional<Member> memberOptional = memberRepository.findByName("username");
        Member member = memberOptional.get();

        //when
        Long memberId = memberService.remove(member.getId());

        //then
        assertThat(memberId.equals(member.getId()));
    }

    //TODO: 로그인, 로그아웃
}