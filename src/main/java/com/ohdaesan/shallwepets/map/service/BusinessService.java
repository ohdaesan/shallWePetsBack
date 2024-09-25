package com.ohdaesan.shallwepets.map.service;

import com.ohdaesan.shallwepets.map.model.Business;
import com.ohdaesan.shallwepets.map.repository.BusinessRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class BusinessService {
    private BusinessRepository businessRepository;

    public List<Business> getBusinessList() {
        return businessRepository.findAll();
    }
}