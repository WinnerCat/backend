package com.winner_cat.domain.member.service;

import com.winner_cat.domain.member.dto.JoinDTO;
import com.winner_cat.domain.member.entity.Member;
import com.winner_cat.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ResponseEntity<?> join(JoinDTO joinDTO) {

        // 동일 username 사용자 생성 방지
        if (memberRepository.existsMemberByUsername(joinDTO.getUsername())) {
            System.out.println("이미 존재하는 회원");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 존재하는 회원입니다.");
        }

        // 새로운 회원 생성 - OAuth2 를 통한 회원가입을 수행할 경우 비밀번호는 저장하지 않아야함
        Member member = Member.builder()
                .username(joinDTO.getUsername())
                // 비밀번호 암호화 해서 저장
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .role("ROLE_ADMIN") // 사용자 권한 설정 접두사 ROLE 작성 필요
                .build();
        memberRepository.save(member);

        return ResponseEntity.ok("회원가입 성공");
    }

}
