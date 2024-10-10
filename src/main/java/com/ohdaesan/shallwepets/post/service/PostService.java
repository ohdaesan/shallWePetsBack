package com.ohdaesan.shallwepets.post.service;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostSummaryDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.Status;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import com.ohdaesan.shallwepets.review.domain.entity.Review;
import com.ohdaesan.shallwepets.review.service.ReviewService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ReviewService reviewService;
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

    // 전체 포스트 한번에 가져오기
//    public List<Post> getPostsByCategoryAndCities(String category, List<String> cities) {
//        Set<Post> postsSet = new HashSet<>();
//
//        for (String city : cities) {
//            List<Post> posts = postRepository.findByCtgryTwoNmAndCtyprvnNmContains(category, city);
//            postsSet.addAll(posts);
//        }
//
//        return new ArrayList<>(postsSet);
//    }

    public List<Post> getPostsByCategoryAndCities(String category, List<String> cities, int pageNo) {
        Page<Post> page = null;

        PageRequest pageRequest = PageRequest.of(pageNo, 10);

        if (cities.size() == 1) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNmContainsOrderByFcltyNm(category, cities.get(0), pageRequest);
        } else if (cities.size() == 2) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2ContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), pageRequest);
        } else if (cities.size() == 3) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), pageRequest);
        } else if (cities.size() == 4) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), cities.get(3), pageRequest);
        }

        assert page != null;
        List<Post> posts = page.getContent();

        return posts;
    }

    public List<Post> getPostsByCategoryAndCitiesAndSigngu(String category, List<String> cities, String signgu, int pageNo) {
        Page<Post> page = null;

        PageRequest pageRequest = PageRequest.of(pageNo, 10);

        if (cities.size() == 1) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNmContainsAndSignguNmEqualsOrderByFcltyNm(category, cities.get(0), signgu, pageRequest);
        } else if (cities.size() == 2) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndSignguNmEqualsOrderByFcltyNm(category, cities.get(0), cities.get(1), signgu, pageRequest);
        } else if (cities.size() == 3) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndSignguNmEqualsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), signgu, pageRequest);
        } else if (cities.size() == 4) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndSignguNmEqualsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), cities.get(3), signgu, pageRequest);
        }

        assert page != null;
        List<Post> posts = page.getContent();

        return posts;
    }

    public List<Post> getPostsByCategoryAndCitiesAndKeyword(String category, List<String> cities, String keyword, int pageNo) {
        Page<Post> page = null;

        PageRequest pageRequest = PageRequest.of(pageNo, 10);

        if (cities.size() == 1) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNmContainsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), keyword, pageRequest);
        } else if (cities.size() == 2) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), keyword, pageRequest);
        } else if (cities.size() == 3) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), keyword, pageRequest);
        } else if (cities.size() == 4) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), cities.get(3), keyword, pageRequest);
        }

        assert page != null;
        List<Post> posts = page.getContent();

        return posts;
    }

    public List<Post> getPostsByCategoryAndCitiesAndSignguAndKeyword(String category, List<String> cities, String signgu, String keyword, int pageNo) {
        Page<Post> page = null;

        PageRequest pageRequest = PageRequest.of(pageNo, 10);

        if (cities.size() == 1) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNmContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), signgu, keyword, pageRequest);
        } else if (cities.size() == 2) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), signgu, keyword, pageRequest);
        } else if (cities.size() == 3) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), signgu, keyword, pageRequest);
        } else if (cities.size() == 4) {
            page = postRepository.findByCtgryTwoNmAndCtyprvnNm1Or2Or3Or4ContainsAndSignguNmEqualsAndFcltyNmContainsOrderByFcltyNm(category, cities.get(0), cities.get(1), cities.get(2), cities.get(3), signgu, keyword, pageRequest);
        }

        assert page != null;
        List<Post> posts = page.getContent();

        return posts;
    }

    public List<String> getDistinctSignguByCitiesAndCategory(List<String> cities, String category) {
        Set<String> signguSet = new HashSet<>();

        for (String city : cities) {
            List<String> signgus = postRepository.findDistinctSignguByCtyprvnNmContainsAndCtgryTwoNm(city, category);
            signguSet.addAll(signgus);
        }

        return new ArrayList<>(signguSet);
    }

    public Page<PostSummaryDTO> getAllPost(Pageable pageable, String searchTerm, String sort) {
        // 포스트 정보를 페이지 단위로 조회
        Page<Post> postPage;

        // 검색어가 있는 경우 포스트를 검색
        if (searchTerm == null || searchTerm.isEmpty()) {
            postPage = postRepository.findByStatus(Status.APPROVED, pageable); // 전체 포스트 조회
        } else {
            postPage = postRepository.findByFcltyNmContainingIgnoreCase(searchTerm, pageable); // 검색어에 맞는 포스트 조회
        }

        // 포스트 정보를 PostSummaryDTO로 변환하면서 리뷰 정보 추가
        List<PostSummaryDTO> postSummaryDTOs = postPage.getContent().stream().map(post -> {
            // 리뷰 정보를 가져오기
            List<Review> reviews = reviewService.getReviewsForPost(post.getPostNo());
            int reviewCount = reviews.size();
            double averageRate = reviewService.calculateAverageRateByPostNo(post.getPostNo()); // 메서드 호출

            // PostDTO 생성
            PostDTO postDTO = PostDTO.builder()
                    .postNo(post.getPostNo())
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
                    .memberNo(post.getMember().getMemberNo())
                    .createdDate(post.getCreatedDate())
                    .status(String.valueOf(post.getStatus()))
                    .statusExplanation(post.getStatusExplanation())
                    .viewCount(post.getViewCount())
                    .build();

            // PostSummaryDTO 생성 및 반환
            return PostSummaryDTO.builder()
                    .postDTO(postDTO)
                    .reviewCount(reviewCount)
                    .averageRate(averageRate)
                    .build();
        }).collect(Collectors.toList());

        // 정렬 추가
        if (sort != null) {
            if ("reviewCount".equals(sort)) {
                postSummaryDTOs.sort(Comparator.comparingInt(PostSummaryDTO::getReviewCount).reversed());
            } else if ("averageRate".equals(sort)) {
                postSummaryDTOs.sort(Comparator.comparingDouble(PostSummaryDTO::getAverageRate).reversed());
            }
        }

        return new PageImpl<>(postSummaryDTOs, pageable, postPage.getTotalElements());
    }


    public PostDTO updatePostStatus(Long postNo, PostDTO postDTO) {
        Post existingPost = postRepository.findById(postNo)
                .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

        existingPost.setStatus(Status.valueOf(postDTO.getStatus()));
        existingPost.setStatusExplanation(postDTO.getStatusExplanation());
        postRepository.save(existingPost);

        return postDTO;
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
                updatePost.getPostNo(),
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

    public List<PostDTO> getPostAwaitingList() {
        // APPROVED가 아닌 게시물들을 조회
        List<Post> posts = postRepository.findByStatusNot(Status.APPROVED);

        // Post 엔티티를 PostDTO로 변환
        return posts.stream().map(post -> PostDTO.builder()
                .postNo(post.getPostNo())
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
                .memberNo(post.getMember().getMemberNo())
                .createdDate(post.getCreatedDate())
                .status(String.valueOf(post.getStatus()))
                .statusExplanation(post.getStatusExplanation())
                .viewCount(post.getViewCount())
                .build()).collect(Collectors.toList());
    }


    public Long deletePost(Long postNo) {
        // 게시글 삭제
        postRepository.deleteById(postNo);

        // 삭제된 게시글의 postNo 반환
        return postNo;
    }


    public List<Post> getAllPostsAdmin(int pageNo) {
        Page<Post> page = null;

        PageRequest pageRequest = PageRequest.of(pageNo, 10);

        page = postRepository.findAllPostsAdmin(pageRequest);

        assert page != null;
        List<Post> posts = page.getContent();

        return posts;
    }
}