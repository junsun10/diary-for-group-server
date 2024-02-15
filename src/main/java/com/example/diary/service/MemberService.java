package com.example.diary.service;

import com.example.diary.domain.member.Member;
import com.example.diary.dto.member.MemberDto;
import com.example.diary.dto.member.MemberLoginDto;
import com.example.diary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long join(Member member) {
        memberRepository.save(member);
        log.info("join member");
        return member.getId();
    }

    // TODO: 그룹, 게시글, 댓글 정리
    /**
     * 회원 탈퇴
     */
    @Transactional
    public Long remove(Long id) {
        memberRepository.deleteById(id);
        log.info("remove member");
        return id;
    }

    /**
     * 로그인
     */
    public MemberDto login(MemberLoginDto memberLoginDto) {
        Member findMember = memberRepository.findByName(memberLoginDto.getName());
        if (findMember != null && findMember.getPassword().equals(memberLoginDto.getPassword())) {
            MemberDto findMemberDto = new MemberDto(findMember.getId(), findMember.getName());
            return findMemberDto;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}
