package com.ohdaesan.shallwepets.point.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.point.domain.dto.PointDTO;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import com.ohdaesan.shallwepets.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Point")
@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {
    private final PointService pointService;

    // 포인트 내역 조회 ( 기록이 남는 용)
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Get points by member number", description = "Retrieve all points for a specific member")
    @GetMapping("/{memberNo}")
    public ResponseEntity<ResponseDTO> getPointsByMemberNo(@PathVariable Long memberNo) {
        // Get points associated with the specified memberNo
        List<PointDTO> points = pointService.getPointsByMemberNo(memberNo);

        // Prepare the response map
        Map<String, Object> responseMap = Map.of("points", points);

        return ResponseEntity.ok(new ResponseDTO(200, "Points retrieved successfully", responseMap));
    }


    // 유저의 포인트 총 합을 조회
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @Operation(summary = "Get total points by member number", description = "유저의 총 포인트 합")
    @GetMapping("/total/{memberNo}")
    public ResponseEntity<ResponseDTO> getTotalPointsByMemberNo(@PathVariable Long memberNo) {
        int totalPoints = pointService.getTotalPoints(memberNo);

        // Prepare the response map
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("totalPoints", totalPoints);

        return ResponseEntity.ok(new ResponseDTO(200, "Total points retrieved successfully", responseMap));

    }

    // 포인트 추가 기능
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add points to a member", description = "Add points to a member's account")
    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addPoints(@RequestBody PointDTO pointDTO) {
        PointDTO addPoint = pointService.addPoints(pointDTO);
        return ResponseEntity.ok(new ResponseDTO(200, "Points added successfully", addPoint));
    }

}