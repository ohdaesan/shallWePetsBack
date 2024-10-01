package com.ohdaesan.shallwepets.post.service;

import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    public PostDTO getPostDetails(Long postNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Long memberNo = post.getMember().getMemberNo();

        PostDTO postDTO = PostDTO.builder()
                .fcltyNm(post.getFcltyNm())
                .ctgryTwoNm(post.getCtgryTwoNm())
                .ctgryThreeNm(post.getCtgryThreeNm())
                .ctyprvnNm(post.getCtyprvnNm())
                .signguNm(post.getSignguNm())
                .legalDongNm(post.getLegalDongNm())
                .liNm(post.getLiNm())
                .lnbrNm(post.getLnbrNm())
                .roadNm(post.getRoadNm())
                .buldNo(post.getBuldNo())
                .lcLa(post.getLcLa())
                .lcLo(post.getLcLo())
                .zipNo(post.getZipNo())
                .rdnmadrNm(post.getRdnmadrNm())
                .lnmAddr(post.getLnmAddr())
                .telNo(post.getTelNo())
                .hmpgUrl(post.getHmpgUrl())
                .rstdeGuidCn(post.getRstdeGuidCn())
                .operTime(post.getOperTime())
                .parkngPosblAt(post.getParkngPosblAt())
                .utilizaPrcCn(post.getUtilizaPrcCn())
                .petPosblAt(post.getPetPosblAt())
                .entrnPosblPetSizeValue(post.getEntrnPosblPetSizeValue())
                .petLmttMtrCn(post.getPetLmttMtrCn())
                .inPlaceAcpPosblAt(post.getInPlaceAcpPosblAt())
                .outPlaceAcpPosblAt(post.getOutPlaceAcpPosblAt())
                .fcltyInfoDc(post.getFcltyInfoDc())
                .petAcpAditChrgeValue(post.getPetAcpAditChrgeValue())
                .viewCount(post.getViewCount())
                .status(String.valueOf(post.getStatus()))
                .statusExplanation(post.getStatusExplanation())
                .memberNo(memberNo)  // Map memberNo from Member entity
                .createdDate(post.getCreatedDate())
                .build();

        return postDTO;

    }

    public List<Post> getPostsByCategoryAndCities(String category, List<String> cities) {
        Set<Post> postsSet = new HashSet<>();

        for (String city : cities) {
            List<Post> posts = postRepository.findByCtgryTwoNmAndCtyprvnNmContains(category, city);
            postsSet.addAll(posts);
        }

        return new ArrayList<>(postsSet);
    }

    public List<String> getDistinctSignguByCitiesAndCategory(List<String> cities, String category) {
        Set<String> signguSet = new HashSet<>();

        for (String city : cities) {
            List<String> signgus = postRepository.findDistinctSignguByCtyprvnNmContainsAndCtgryTwoNm(city, category);
            signguSet.addAll(signgus);
        }

        return new ArrayList<>(signguSet);
    }
}
