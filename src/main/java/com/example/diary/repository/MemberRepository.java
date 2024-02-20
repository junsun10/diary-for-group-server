package com.example.diary.repository;

import com.example.diary.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);
    boolean existsByName(String name);
    boolean existsByEmail(String email);
}
