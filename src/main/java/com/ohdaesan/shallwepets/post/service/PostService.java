package com.ohdaesan.shallwepets.post.service;

import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;



    public PostDTO getPostDetails(Long postNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Long memberNo = post.getMember().getMemberNo();

        // Map post details to PostDTO
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

}
