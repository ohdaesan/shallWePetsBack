package com.ohdaesan.shallwepets.member.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.dto.ChangePasswordDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.member.service.MyPageService;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewImagesDTO;
import com.ohdaesan.shallwepets.review.domain.entity.Review;
import com.ohdaesan.shallwepets.review.repository.ReviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Tag(name = "MyPage")
@RestController
@RequestMapping("/mypage")
@Slf4j
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;


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

    @PostMapping("/{memberNo}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long memberNo, @RequestBody ChangePasswordDTO passwordData) {
        try {
            // 회원 정보 조회
            Member member = memberService.findById(memberNo);

            // 로그 출력
            log.info("Current Password: {}", passwordData.getCurrentPassword());
            log.info("Encoded Password from DB: {}", member.getMemberPwd());

            // 현재 비밀번호 확인
            if (!passwordEncoder.matches(passwordData.getCurrentPassword(), member.getMemberPwd())) {
                log.info("기존 비밀번호와 입력된 비밀번호가 일치하지 않음..");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("현재 비밀번호가 일치하지 않습니다.");
            }

            // 새 비밀번호 변경
            myPageService.changePassword(memberNo, passwordData);
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원을 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다.");
        }
    }


    // 여기서부터 post


//    // 업체 등록
//    @Operation(summary = "업체 등록", description = "사용자가 입력한 정보를 이용하여 업체 등록")
//    @PostMapping("/businessregister")
//    public ResponseEntity<ResponseDTO> registerPost(
//            @RequestParam Map<String, String> postDTO,
//            @RequestParam("memberNo") Long memberNo,
//            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
//        try {
//            PostDTO postDTOObj = new PostDTO();
//            // Map the received parameters to PostDTO object
//            BeanUtils.copyProperties(postDTO, postDTOObj);
//
//            PostDTO registeredPost = myPageService.registerPost(postDTOObj, images, memberNo);
//            return ResponseEntity.ok()
//                    .body(new ResponseDTO(HttpStatus.CREATED.value(), "업체 등록 신청 성공", registeredPost));
//        } catch (Exception e) {
//            // Error handling (unchanged)
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다", null));
//        }
//    }


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


    // 여기서부턴 내 리뷰

    // MemberNo로 리뷰 조회
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @GetMapping("/myreviewlist")
    public ResponseEntity<ResponseDTO> getMemberReviews(@RequestParam Long memberNo) {
        List<ReviewDTO> reviews = myPageService.getMemberReviewsByMemberNo(memberNo);
        return ResponseEntity.ok().body(new ResponseDTO(200, "회원 리뷰 목록 조회 성공", reviews));
    }

}


