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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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

    @PostMapping("/findId")
    @ResponseBody
    public ResponseEntity<Map<String, String>> findId(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        try {
            String searchBy = params.get("searchBy");

            String memberId = "";

            if ("email".equals(searchBy)) {
                String name2 = params.get("name2");
                String email = params.get("email");
                memberId = memberService.findMemberIdByMemberNameAndMemberEmail(name2, email);
            } else if ("phone".equals(searchBy)){
                String name1 = params.get("name1");
                String phone = params.get("phone");
                memberId = memberService.findMemberIdByMemberNameAndMemberPhone(name1, phone);
            }

            response.put("memberId", memberId);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("error", "아이디를 찾을 수 없습니다.\n입력하신 정보를 다시 확인해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/findPwd")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> findPwd(@RequestBody Map<String, String> params) {
        Map<String, Boolean> response = new HashMap<>();

        try {
            String searchBy = params.get("searchBy");
            boolean exists = false;

            if ("email".equals(searchBy)) {
                String id2 = params.get("id2");
                String name2 = params.get("name2");
                String email = params.get("email");
                exists = memberService.existsByMemberIdAndMemberNameAndMemberEmail(id2, name2, email);
            } else if ("phone".equals(searchBy)){
                String id1 = params.get("id1");
                String name1 = params.get("name1");
                String phone = params.get("phone");
                exists = memberService.existsByMemberIdAndMemberNameAndMemberPhone(id1, name1, phone);
            }

            response.put("exists", exists);

            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            response.put("error", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/changePwdNotLoggedIn")
    public ResponseEntity<Map<String, String>> changePwNotLoggedIn(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        String memberId = params.get("memberId");
        String modifiedPw = params.get("modifiedPw");
        String modifiedPwConfirm = params.get("modifiedPwConfirm");

        // 서버사이드에서도 확인
        if (modifiedPw == null || modifiedPwConfirm == null || !modifiedPw.equals(modifiedPwConfirm)) {
            response.put("error", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        String regexPw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!modifiedPw.matches(regexPw)) {
            response.put("error", "비밀번호는 최소 8자 이상이어야 하며, 적어도 하나의 문자, 숫자 및 특수 문자를 포함해야 합니다.");
            return ResponseEntity.badRequest().body(response);
        }

        if (memberService.isPasswordInUse(memberId, modifiedPw)) {
            response.put("error", "기존과 같은 비밀번호는 사용하실 수 없습니다.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            memberService.updatePassword(memberId, modifiedPw);
            response.put("success", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            response.put("error", "존재하지 않는 회원입니다.");
        }

        return ResponseEntity.ok(response);
    }
}
