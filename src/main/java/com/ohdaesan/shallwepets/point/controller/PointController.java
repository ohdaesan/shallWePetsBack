package com.ohdaesan.shallwepets.point.controller;

import com.ohdaesan.shallwepets.point.service.PointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Point")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/point")
public class PointController {
    private final PointService pointService;


}
