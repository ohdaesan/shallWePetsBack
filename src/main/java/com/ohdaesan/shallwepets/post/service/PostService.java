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

// 관리자
    public PostDTO updatePostStatus(Long postNo, PostDTO postDTO) {
        // 게시물 찾기
        Post existingPost = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        // 게시물 수정
        Post updatePost = Post.builder()
                .postNo(existingPost.getPostNo()) // 기존 게시물 번호 유지
                .fcltyNm(existingPost.getFcltyNm())
                .ctgryTwoNm(existingPost.getCtgryTwoNm())
                .ctgryThreeNm(existingPost.getCtgryThreeNm())
                .ctyprvnNm(existingPost.getCtyprvnNm())
                .signguNm(existingPost.getSignguNm())
                .legalDongNm(existingPost.getLegalDongNm())
                .liNm(existingPost.getLiNm())
                .lnbrNm(existingPost.getLnbrNm())
                .roadNm(existingPost.getRoadNm())
                .buldNo(existingPost.getBuldNo())
                .lcLa(existingPost.getLcLa())
                .lcLo(existingPost.getLcLo())
                .zipNo(existingPost.getZipNo())
                .rdnmadrNm(existingPost.getRdnmadrNm())
                .lnmAddr(existingPost.getLnmAddr())
                .telNo(existingPost.getTelNo())
                .hmpgUrl(existingPost.getHmpgUrl())
                .rstdeGuidCn(existingPost.getRstdeGuidCn())
                .operTime(existingPost.getOperTime())
                .parkngPosblAt(existingPost.getParkngPosblAt())
                .utilizaPrcCn(existingPost.getUtilizaPrcCn())
                .petPosblAt(existingPost.getPetPosblAt())
                .entrnPosblPetSizeValue(existingPost.getEntrnPosblPetSizeValue())
                .petLmttMtrCn(existingPost.getPetLmttMtrCn())
                .inPlaceAcpPosblAt(existingPost.getInPlaceAcpPosblAt())
                .outPlaceAcpPosblAt(existingPost.getOutPlaceAcpPosblAt())
                .fcltyInfoDc(existingPost.getFcltyInfoDc())
                .petAcpAditChrgeValue(existingPost.getPetAcpAditChrgeValue())
                .member(existingPost.getMember())
                .createdDate(existingPost.getCreatedDate())
                .status(Status.valueOf(postDTO.getStatus()))
                .statusExplanation(postDTO.getStatusExplanation())
                .viewCount(existingPost.getViewCount())
                .build();

        // 수정된 게시물 저장
        postRepository.save(updatePost);

        // PostDTO 반환
        return new PostDTO(
                updatePost.getFcltyNm(),
                updatePost.getCtgryTwoNm(),
                updatePost.getCtgryThreeNm(),
                updatePost.getCtyprvnNm(),
                updatePost.getSignguNm(),
                updatePost.getLegalDongNm(),
                updatePost.getLiNm(),
                updatePost.getLnbrNm(),
                updatePost.getRoadNm(),
                updatePost.getBuldNo(),
                updatePost.getLcLa(),
                updatePost.getLcLo(),
                updatePost.getZipNo(),
                updatePost.getRdnmadrNm(),
                updatePost.getLnmAddr(),
                updatePost.getTelNo(),
                updatePost.getHmpgUrl(),
                updatePost.getRstdeGuidCn(),
                updatePost.getOperTime(),
                updatePost.getParkngPosblAt(),
                updatePost.getUtilizaPrcCn(),
                updatePost.getPetPosblAt(),
                updatePost.getEntrnPosblPetSizeValue(),
                updatePost.getPetLmttMtrCn(),
                updatePost.getInPlaceAcpPosblAt(),
                updatePost.getOutPlaceAcpPosblAt(),
                updatePost.getFcltyInfoDc(),
                updatePost.getPetAcpAditChrgeValue(),
                updatePost.getMember().getMemberNo(),
                updatePost.getCreatedDate(),
                updatePost.getStatus().name(),
                updatePost.getStatusExplanation(),
                updatePost.getViewCount()
        );
    }

    public PostDTO updatePostForm(Long postNo, PostDTO postDTO) {
        // 게시물 찾기
        Post existingPost = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        // 게시물 수정
        Post updatePost = Post.builder()
                .postNo(existingPost.getPostNo()) // 기존 게시물 번호 유지
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
                .member(existingPost.getMember())
                .createdDate(existingPost.getCreatedDate())
                .status(Status.valueOf(String.valueOf(existingPost.getStatus())))
                .statusExplanation(existingPost.getStatusExplanation())
                .viewCount(existingPost.getViewCount())
                .build();

        // 수정된 게시물 저장
        postRepository.save(updatePost);

        // PostDTO 반환
        return new PostDTO(
                updatePost.getFcltyNm(),
                updatePost.getCtgryTwoNm(),
                updatePost.getCtgryThreeNm(),
                updatePost.getCtyprvnNm(),
                updatePost.getSignguNm(),
                updatePost.getLegalDongNm(),
                updatePost.getLiNm(),
                updatePost.getLnbrNm(),
                updatePost.getRoadNm(),
                updatePost.getBuldNo(),
                updatePost.getLcLa(),
                updatePost.getLcLo(),
                updatePost.getZipNo(),
                updatePost.getRdnmadrNm(),
                updatePost.getLnmAddr(),
                updatePost.getTelNo(),
                updatePost.getHmpgUrl(),
                updatePost.getRstdeGuidCn(),
                updatePost.getOperTime(),
                updatePost.getParkngPosblAt(),
                updatePost.getUtilizaPrcCn(),
                updatePost.getPetPosblAt(),
                updatePost.getEntrnPosblPetSizeValue(),
                updatePost.getPetLmttMtrCn(),
                updatePost.getInPlaceAcpPosblAt(),
                updatePost.getOutPlaceAcpPosblAt(),
                updatePost.getFcltyInfoDc(),
                updatePost.getPetAcpAditChrgeValue(),
                updatePost.getMember().getMemberNo(),
                updatePost.getCreatedDate(),
                updatePost.getStatus().name(),
                updatePost.getStatusExplanation(),
                updatePost.getViewCount()
        );
    }

    public List<PostDTO> getPostByMemberNo(Long memberNo) {
        List<Post> posts = postRepository.findByMember_MemberNo(memberNo);

        if (posts.isEmpty()) {
            throw new RuntimeException("포스트를 찾을 수 없습니다.");
        }

        List<PostDTO> postDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());

        return postDTOs;
    }




}
