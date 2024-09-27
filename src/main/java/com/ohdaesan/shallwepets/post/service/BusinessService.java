package com.ohdaesan.shallwepets.post.service;

import com.ohdaesan.shallwepets.post.domain.dto.BusinessDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessService {
    private final PostRepository postRepository;

    public List<BusinessDTO> getAllBusinesses() {
        List<Post> businesses = postRepository.findAll();

        return businesses.stream().map(business -> BusinessDTO.builder()
                .postNo(business.getPostNo())
                .fcltyNm(business.getFcltyNm())
                .rdnmadrNm(business.getRdnmadrNm())
                .telNo(business.getTelNo())
                .build()).collect(Collectors.toList());


    }
}