package com.example.demo.controller;


import com.example.demo.repository.JPAMemberRepository;
import com.example.demo.service.MemberService;
import com.example.demo.domain.Member;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.SignupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MemberController {
    private final JPAMemberRepository memberRepository;
    MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService, JPAMemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginDto loginDto) {
        Map<String, Object> response = new HashMap<>();

        if ("test@naver.com".equals(loginDto.getEmail()) && "12345678".equals(loginDto.getPassword())) {
            response.put("success", true);
            response.put("message", "로그인 성공");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "이메일 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@RequestBody SignupDto signupDto) {
        Map<String, Object> response = new HashMap<>();

        Optional<Member> existingUser = memberRepository.findByEmail(signupDto.getEmail());

        if (existingUser.isPresent()) {
            response.put("success", false);
            response.put("message", "이미 사용 중인 이메일입니다.");
            return ResponseEntity.badRequest().body(response);
        }

        memberService.registerUser(signupDto);
        response.put("success", true);
        response.put("message", "회원가입 성공!");
        return ResponseEntity.ok(response);
    }
}