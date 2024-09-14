package com.ohdaesan.shallwepets.member.controller;

import com.ohdaesan.shallwepets.auth.common.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public void login(@RequestBody MemberDTO memberDTO) {
    }
}
