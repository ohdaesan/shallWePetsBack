package com.ohdaesan.shallwepets.member.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Tag(name = "Member")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    // 회원가입 요청
    @Operation(summary = "회원가입", description = "유저가 입력한 정보를 이용하여 회원가입")
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> signup(@RequestBody MemberDTO memberDTO) {
        MemberDTO savedMemberDTO = memberService.register(memberDTO);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", savedMemberDTO));
    }

    @Operation(summary = "로그인", description = "유저가 입력한 정보를 이용하여 로그인")
    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public void login(@RequestBody MemberDTO memberDTO) {
    }

    @Operation(summary = "아이디 찾기", description = "유저가 입력한 정보를 이용하여 아이디 찾기")
    @PostMapping("/findId")
    @ResponseBody
    public ResponseEntity<ResponseDTO> findId(@RequestBody Map<String, String> params) {
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

            return ResponseEntity.ok().body(new ResponseDTO(200, "memberId 존재 여부 확인 성공", response));
        } catch (NoSuchElementException e) {
            response.put("error", "아이디를 찾을 수 없습니다.\n입력하신 정보를 다시 확인해주세요.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "memberId 찾기 실패", response));
        }
    }

    @Operation(summary = "비밀번호 찾기", description = "유저가 입력한 정보를 이용하여 존재하는 유저인지 확인 (실제 비밀번호를 찾아주지는 않음. 비밀번호 변경만 가능.)")
    @PostMapping("/findPwd")
    @ResponseBody
    public ResponseEntity<ResponseDTO> findPwd(@RequestBody Map<String, String> params) {
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

            return ResponseEntity.ok().body(new ResponseDTO(200, "회원 존재 여부 확인 성공", response));
        } catch (NoSuchElementException e) {
            response.put("error", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "회원 찾기 실패", response));
        }
    }

    @Operation(summary = "닉네임 찾기", description = "회원번호를 이용하여 닉네임 찾기")
    @PostMapping("/findNickname")
    public ResponseEntity<ResponseDTO> findNicknameByMemberNo(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        Long memberNo = Long.valueOf(params.get("memberNo"));

        try {
            String memberNickname = memberService.findNicknameByMemberNo(memberNo);
            response.put("nickname", memberNickname);

            return ResponseEntity.ok().body(new ResponseDTO(200, "닉네임 찾기 성공", response));
        } catch (NoSuchElementException e) {
            response.put("error", "닉네임을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "닉네임 찾기 실패", response));
        }
    }

    @Operation(summary = "회원 등급 찾기", description = "회원번호를 이용하여 회원 등급 찾기")
    @PostMapping("/findGrade")
    public ResponseEntity<ResponseDTO> findGradeByMemberNo(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        Long memberNo = Long.valueOf(params.get("memberNo"));

        try {
            String memberGrade = memberService.findGradeByMemberNo(memberNo);
            response.put("grade", memberGrade);

            return ResponseEntity.ok().body(new ResponseDTO(200, "회원 등급 찾기 성공", response));
        } catch (NoSuchElementException e) {
            response.put("error", "회원 등급을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(HttpStatus.NOT_FOUND, "회원 등급 찾기 실패", response));
        }
    }

    @Operation(summary = "로그인하지 않은 상태에서 비밀번호 변경", description = "비밀번호 찾기에서 존재하는 회원인지 확인되면 비밀번호 변경")
    @PostMapping("/changePwdNotLoggedIn")
    public ResponseEntity<ResponseDTO> changePwNotLoggedIn(@RequestBody Map<String, String> params) {
        Map<String, String> response = new HashMap<>();

        String memberId = params.get("memberId");
        String modifiedPw = params.get("modifiedPw");
        String modifiedPwConfirm = params.get("modifiedPwConfirm");

        // 서버사이드에서도 확인
        if (modifiedPw == null || modifiedPwConfirm == null || !modifiedPw.equals(modifiedPwConfirm)) {
            response.put("error", "비밀번호와 비밀번호 확인이 서로 일치하지 않습니다.");
            return ResponseEntity.badRequest().body(new ResponseDTO(400, "비밀번호 변경 실패", response));
        }

        String regexPw = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
        if (!modifiedPw.matches(regexPw)) {
            response.put("error", "비밀번호는 최소 8자 이상이어야 하며, 적어도 하나의 문자, 숫자 및 특수 문자를 포함해야 합니다.");
            return ResponseEntity.badRequest().body(new ResponseDTO(400, "비밀번호 변경 실패", response));
        }

        if (memberService.isPasswordInUse(memberId, modifiedPw)) {
            response.put("error", "기존과 같은 비밀번호는 사용하실 수 없습니다.");
            return ResponseEntity.badRequest().body(new ResponseDTO(400, "비밀번호 변경 실패", response));
        }

        try {
            memberService.updatePassword(memberId, modifiedPw);
            response.put("success", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            response.put("error", "존재하지 않는 회원입니다.");
        }

        return ResponseEntity.ok().body(new ResponseDTO(200, "비밀번호 변경 성공", response));
    }

    @Operation(summary = "아이디 중복 확인", description = "이미 존재하는 아이디인지 확인")
    @GetMapping("/checkId")
    public ResponseEntity<ResponseDTO> checkMemberId(@RequestParam String memberId) {
        boolean exists = memberService.existsMemberId(memberId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok().body(new ResponseDTO(200, "memberId 존재 여부 확인 성공", response));
    }

    @Operation(summary = "닉네임 중복 확인", description = "이미 존재하는 닉네임인지 확인")
    @GetMapping("/checkNickname")
    public ResponseEntity<ResponseDTO> checkNickname(@RequestParam String memberNickname) {
        boolean exists = memberService.existsNickname(memberNickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok().body(new ResponseDTO(200, "memberNickname 존재 여부 확인 성공", response));
    }

    @Operation(summary = "이미 가입한 이력이 있는 회원인지 확인", description = "이메일과 전화번호를 이용하여 이미 가입한 이력이 있는 회원인지 확인")
    @GetMapping("/checkUser")
    public ResponseEntity<ResponseDTO> checkUser(
            @RequestParam String memberEmail,
            @RequestParam String memberPhone) {
        boolean emailExists = memberService.existsEmail(memberEmail);
        boolean phoneExists = memberService.existsPhone(memberPhone);

        Map<String, Boolean> response = new HashMap<>();

        response.put("emailExists", emailExists);
        response.put("phoneExists", phoneExists);

        return ResponseEntity.ok().body(new ResponseDTO(200, "email과 전화번호 이용한 멤버 존재 여부 확인 성공", response));
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Operation(summary = "포인트 회원 정보 조회", description = "전체 회원 정보 조회")
    @GetMapping("/memberList")
    public ResponseEntity<ResponseDTO> searchMemberList() {
        List<Member> memberList = memberService.getAllMembers();
        Map<String, Object> response = new HashMap<>();
        response.put("members", memberList);
        response.put("count", memberList.size()); // 회원 수 추가

        return ResponseEntity.ok().body(new ResponseDTO(200, "회원 정보 조회 성공", response));
    }

}