package com.example.demo.service;

import com.example.demo.domain.Member;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignupDto;
import com.example.demo.repository.JPAMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final JPAMemberRepository memberRepository;

    public MemberService(JPAMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public boolean isJoin(LoginDto loginDto) {
        return memberRepository.findByEmail(loginDto.getEmail()).isPresent();
    }

    public boolean registerUser(SignupDto signupDto) {
        // 이메일 중복 체크
        Optional<Member> existingUser = memberRepository.findByEmail(signupDto.getEmail());
        if (existingUser.isPresent()) {
            return false; // 이미 가입된 이메일
        }

        // 새 회원 저장
        Member member = new Member();
        member.setEmail(signupDto.getEmail());
        member.setPassword(signupDto.getPassword());
        member.setNickname(signupDto.getNickname());
        memberRepository.save(member);
        return true;
    }
}
