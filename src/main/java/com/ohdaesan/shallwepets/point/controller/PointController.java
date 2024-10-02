package com.ohdaesan.shallwepets.point.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.member.service.MemberService;
import com.ohdaesan.shallwepets.point.domain.dto.PointDTO;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import com.ohdaesan.shallwepets.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Point")
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    // 포인트 내역 조회 (기록이 남는 용)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "포인트 내역 조회", description = "해당 멤버 번호의 포인트 내역 조회")
    @GetMapping("/{memberNo}")
    public ResponseEntity<ResponseDTO> getPointsByMemberNo(@PathVariable Long memberNo) {
        List<PointDTO> points = pointService.getPointsByMemberNo(memberNo);

        Map<String, Object> responseMap = Map.of("points", points);

        return ResponseEntity.ok(new ResponseDTO(200, "포인트 내역 조회 성공", responseMap));
    }

    // 유저의 포인트 총합을 조회
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "포인트 총합", description = "해당 멤버 번호의 포인트 총합")
    @GetMapping("/total/{memberNo}")
    public ResponseEntity<ResponseDTO> getTotalPointsByMemberNo(@PathVariable Long memberNo) {
        int totalPoints = pointService.getTotalPoints(memberNo);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalPoints", totalPoints);

        return ResponseEntity.ok(new ResponseDTO(200, "포인트 총합 조회 성공", responseMap));
    }

    // 포인트 추가 기능
//    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "포인트 추가", description = "특정 멤버에게 포인트 추가(관리자 기능)")
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addPoints(@RequestBody PointDTO pointDTO) {
        PointDTO addPoint = pointService.addPoints(pointDTO);

        Optional<Member> member = memberRepository.findMemberByMemberNo(pointDTO.getMemberNo());

        // 등급 업그레이드 대상인지 확인 후 등급 업데이트
        int totalPoints = pointService.getTotalPoints(pointDTO.getMemberNo());
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);

        if (totalPoints >= 200 && totalPoints < 500) {
            memberDTO.setGrade("웰시코기");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        } else if (totalPoints >= 500 && totalPoints < 1000) {
            memberDTO.setGrade("리트리버");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        } else if (totalPoints >= 1000) {
            memberDTO.setGrade("그레이트데인");

            Member memberUpdated = modelMapper.map(memberDTO, Member.class);
            memberRepository.save(memberUpdated);
        }

        return ResponseEntity.ok(new ResponseDTO(200, "포인트 추가 성공", addPoint));
    }
}