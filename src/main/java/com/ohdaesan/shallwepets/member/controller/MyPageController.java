package com.ohdaesan.shallwepets.member.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.dto.ChangePasswordDTO;
import com.ohdaesan.shallwepets.member.service.MyPageService;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "MyPage")
@RestController
@RequestMapping("/mypage")
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/my_info")
    public ResponseEntity<ResponseDTO> getMyInfo(@RequestParam Long memberNo) {
        MemberDTO memberDTO = myPageService.getMemberInfo(memberNo);
        return ResponseEntity.ok().body(new ResponseDTO(200, "회원 정보 조회 성공", memberDTO));
    }

    @PutMapping("/my_info")
    public ResponseEntity<ResponseDTO> updateMyInfo(@RequestParam Long memberNo, @RequestBody MemberDTO updatedMemberDTO) {
        MemberDTO updatedInfo = myPageService.updateMemberInfo(memberNo, updatedMemberDTO);
        return ResponseEntity.ok(new ResponseDTO(200, "회원 정보 수정 성공", updatedInfo));
    }

    @PostMapping("/my_info/profile_picture")
    public ResponseEntity<ResponseDTO> uploadProfilePicture(@RequestParam Long memberNo, @RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = myPageService.uploadProfilePicture(memberNo, file);
        Map<String, String> result = new HashMap<>();
        result.put("fileUrl", fileUrl);
        return ResponseEntity.ok(new ResponseDTO(200, "프로필 사진 업로드 성공", result));
    }

    @GetMapping("/check_id")
    public ResponseEntity<ResponseDTO> checkIdDuplication(@RequestParam String memberId) {
        boolean isDuplicate = myPageService.checkIdDuplication(memberId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", isDuplicate);
        String message = isDuplicate ? "이미 등록된 아이디입니다." : "사용할 수 있는 아이디입니다.";
        return ResponseEntity.ok().body(new ResponseDTO(200, message, response));
    }

    @GetMapping("/check_nickname")
    public ResponseEntity<ResponseDTO> checkNicknameDuplication(@RequestParam String nickname) {
        boolean isDuplicate = myPageService.checkNicknameDuplication(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", isDuplicate);
        String message = isDuplicate ? "이미 등록된 닉네임입니다." : "사용할 수 있는 닉네임입니다.";
        return ResponseEntity.ok().body(new ResponseDTO(200, message, response));
    }

    @GetMapping("/check_email")
    public ResponseEntity<ResponseDTO> checkEmailDuplication(@RequestParam String email) {
        boolean isDuplicate = myPageService.checkEmailDuplication(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", isDuplicate);
        String message = isDuplicate ? "이미 등록된 이메일입니다." : "사용할 수 있는 이메일입니다.";
        return ResponseEntity.ok().body(new ResponseDTO(200, message, response));
    }

    @GetMapping("/check_phone")
    public ResponseEntity<ResponseDTO> checkPhoneDuplication(@RequestParam String phone) {
        boolean isDuplicate = myPageService.checkPhoneDuplication(phone);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", isDuplicate);
        String message = isDuplicate ? "이미 등록된 전화번호입니다." : "사용할 수 있는 전화번호입니다.";
        return ResponseEntity.ok().body(new ResponseDTO(200, message, response));
    }

    @PostMapping("/change_password")
    public ResponseEntity<ResponseDTO> changePassword(@RequestParam Long memberNo, @RequestBody ChangePasswordDTO changePasswordDTO) {
        myPageService.changePassword(memberNo, changePasswordDTO);
        return ResponseEntity.ok(new ResponseDTO(200, "비밀번호가 성공적으로 변경되었습니다.", null));
    }


    // 여기서부터 post

    // 업체 등록
    @Operation(summary = "업체 등록", description = "사용자가 입력한 정보를 이용하여 업체 등록")
    @PostMapping("/businessregister")
    public ResponseEntity<ResponseDTO> registerPost(
            @RequestPart("postDTO") PostDTO postDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestParam("memberNo") Long memberNo) {

        try {
            PostDTO registeredPost = myPageService.registerPost(postDTO, images, memberNo);
            return ResponseEntity.ok()
                    .body(new ResponseDTO(HttpStatus.CREATED.value(), "업체 등록 신청 성공", registeredPost));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 업로드 중 오류가 발생했습니다", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다", null));
        }
    }


//    @GetMapping("/mybusinesslist/{postNo}")
//    public ResponseEntity<PostDTO> getBusinessDetail(@PathVariable Long postNo, @RequestParam("memberNo") Long memberNo) {
//        PostDTO post = myPageService.getBusinessDetail(postNo, memberNo);
//        return ResponseEntity.ok(post);
//    }
//
//    @PutMapping("/mybusinesslist/{postNo}")
//    public ResponseEntity<PostDTO> updateBusiness(
//            @PathVariable Long postNo,
//            @RequestParam("memberNo") Long memberNo,
//            @RequestPart("postDTO") PostDTO postDTO,
//            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
//        PostDTO updatedPost = myPageService.updateBusiness(postNo, memberNo, postDTO, images);
//        return ResponseEntity.ok(updatedPost);
//    }
//
//    @DeleteMapping("/mybusinesslist/{postNo}")
//    public ResponseEntity<Void> deleteBusiness(@PathVariable Long postNo, @RequestParam("memberNo") Long memberNo) {
//        myPageService.deleteBusiness(postNo, memberNo);
//        return ResponseEntity.noContent().build();  // 삭제 성공 시 204 No Content 응답
//    }

}
