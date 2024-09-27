package com.ohdaesan.shallwepets.post.controller;

import com.ohdaesan.shallwepets.global.ResponseDTO;
import com.ohdaesan.shallwepets.post.domain.dto.BusinessDTO;
import com.ohdaesan.shallwepets.post.service.BusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Business")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/select_location")
public class BusinessController {

    private final BusinessService businessService;

    @Operation(summary = "getAllBusinesses", description = "모든 업체 정보를 불러옵니다.")
    @GetMapping
    public ResponseEntity<ResponseDTO> getAllBusinesses() {
        List<BusinessDTO> businesses = businessService.getAllBusinesses();

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("businesses", businesses);

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(200, "업체 목록 불러오기 성공", responseMap));
    }
}