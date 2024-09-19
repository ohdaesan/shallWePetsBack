package com.ohdaesan.shallwepets.member.controller;

import com.ohdaesan.shallwepets.auth.common.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/register")
    public String register() { return "member/signup"; }

    // 회원가입 요청
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> signup(@RequestBody MemberDTO memberDTO) {
        System.out.println(memberDTO);
        MemberDTO savedMemberDTO = memberService.register(memberDTO);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", savedMemberDTO));
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public void login(@RequestBody MemberDTO memberDTO) {
    }
}
