package com.ohdaesan.shallwepets.map.controller;

import com.ohdaesan.shallwepets.map.model.Business;
import com.ohdaesan.shallwepets.map.service.BusinessService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @GetMapping(value = "/map")
    public List<Business> getBusinessList() {
        return businessService.getBusinessList();
    }
}