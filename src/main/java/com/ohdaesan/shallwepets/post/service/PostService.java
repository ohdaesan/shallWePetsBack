package com.ohdaesan.shallwepets.post.service;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.Status;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import com.ohdaesan.shallwepets.review.domain.dto.ReviewDTO;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDTO getPostDetails(Long postNo) {
        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("포스트를 찾을 수 없습니다."));

        return modelMapper.map(post, PostDTO.class);
    }


    @Transactional
    public PostDTO registerPost(PostDTO postDTO) {
        Post post = Post.builder()
                .fcltyNm(postDTO.getFcltyNm())
                .ctgryTwoNm(postDTO.getCtgryTwoNm())
                .ctgryThreeNm(postDTO.getCtgryThreeNm())
                .ctyprvnNm(postDTO.getCtyprvnNm())
                .signguNm(postDTO.getSignguNm())
                .legalDongNm(postDTO.getLegalDongNm())
                .liNm(postDTO.getLiNm())
                .lnbrNm(postDTO.getLnbrNm())
                .roadNm(postDTO.getRoadNm())
                .buldNo(postDTO.getBuldNo())
                .lcLa(postDTO.getLcLa())
                .lcLo(postDTO.getLcLo())
                .zipNo(postDTO.getZipNo())
                .rdnmadrNm(postDTO.getRdnmadrNm())
                .lnmAddr(postDTO.getLnmAddr())
                .telNo(postDTO.getTelNo())
                .hmpgUrl(postDTO.getHmpgUrl())
                .rstdeGuidCn(postDTO.getRstdeGuidCn())
                .operTime(postDTO.getOperTime())
                .parkngPosblAt(postDTO.getParkngPosblAt())
                .utilizaPrcCn(postDTO.getUtilizaPrcCn())
                .petPosblAt(postDTO.getPetPosblAt())
                .entrnPosblPetSizeValue(postDTO.getEntrnPosblPetSizeValue())
                .petLmttMtrCn(postDTO.getPetLmttMtrCn())
                .inPlaceAcpPosblAt(postDTO.getInPlaceAcpPosblAt())
                .outPlaceAcpPosblAt(postDTO.getOutPlaceAcpPosblAt())
                .fcltyInfoDc(postDTO.getFcltyInfoDc())
                .petAcpAditChrgeValue(postDTO.getPetAcpAditChrgeValue())
                .viewCount(0)
                .createdDate(LocalDateTime.now())
                .status(Status.valueOf("AWAITING"))
                .statusExplanation(null)
                .member(Member.builder().memberNo(postDTO.getMemberNo()).build()) // memberNo 설정
                .build();

        // Post 엔티티 저장
        postRepository.save(post);

        // PostDTO 반환
        return modelMapper.map(post, PostDTO.class);
    }

    public List<PostDTO> getAllPost() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new RuntimeException("포스트를 찾을 수 없습니다.");
        }

        // List<Post>를 List<PostDTO>로 변환
        List<PostDTO> postDTOs= posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());


        return postDTOs;
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
