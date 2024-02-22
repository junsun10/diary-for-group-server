package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.member.MemberCreateDto;
import com.example.diary.dto.member.MemberDto;
import com.example.diary.dto.member.MemberLoginDto;
import com.example.diary.repository.MemberRepository;
import com.example.diary.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public MemberDto join(MemberCreateDto memberCreateDto) {

        if (memberRepository.existsByName(memberCreateDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (memberRepository.existsByEmail(memberCreateDto.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = new Member(memberCreateDto);
        memberRepository.save(member);

        MemberDto memberDto = new MemberDto(member);

        log.info("join member");

        return memberDto;
    }

    /**
     * 회원 탈퇴
     * 회원이 작성한 일기, 일기의 댓글, 좋아요 삭제
     * 회원이 작성한 댓글, 좋아요 삭제
     */
    @Transactional
    public Long remove(Long memberId) {

        memberRepository.deleteById(memberId);

        log.info("remove member");

        return memberId;
    }

    /**
     * 로그인
     */
    public MemberDto login(MemberLoginDto memberLoginDto, HttpServletRequest request) {
        Optional<Member> memberOptional = memberRepository.findByName(memberLoginDto.getName());

        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        Member findMember = memberOptional.get();

        if (!findMember.getPassword().equals(memberLoginDto.getPassword())) {
            throw new IllegalArgumentException("암호가 일치하지 않습니다.");
        }

        MemberDto findMemberDto = new MemberDto(findMember);

        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER_ID, findMember.getId());

        log.info("login");

        return findMemberDto;
    }

    /**
     * 로그아웃
     */
    public void logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
