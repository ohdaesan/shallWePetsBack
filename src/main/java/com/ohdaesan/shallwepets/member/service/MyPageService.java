package com.ohdaesan.shallwepets.member.service;

import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.member.domain.dto.MemberDTO;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.mypage.domain.dto.ChangePasswordDTO;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.PostImages;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {
    private final ModelMapper modelMapper;
    private final MemberRepository memberRepository;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "gif");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private final PostRepository postRepository;

    public MemberDTO getMemberInfo(Long memberNo) {
        Member member = memberRepository.findByMemberNo(memberNo);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }
        return modelMapper.map(member, MemberDTO.class);
    }

    @Transactional
    public MemberDTO updateMemberInfo(Long memberNo, MemberDTO updatedMemberDTO) {
        // 회원 정보 조회
        Member existingMember = memberRepository.findByMemberNo(memberNo);
        if (existingMember == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 로깅: 기존 회원 정보 출력
        log.info("기존 회원 정보: {}", existingMember);

        // 기존 멤버의 필드 수정: null 체크 추가
        if (updatedMemberDTO.getMemberNickname() != null) {
            existingMember.setMemberNickname(updatedMemberDTO.getMemberNickname());
        }
        if (updatedMemberDTO.getMemberName() != null) {
            existingMember.setMemberName(updatedMemberDTO.getMemberName());
        }
        if (updatedMemberDTO.getMemberEmail() != null) {
            existingMember.setMemberEmail(updatedMemberDTO.getMemberEmail());
        }
        if (updatedMemberDTO.getMemberPhone() != null) {
            existingMember.setMemberPhone(updatedMemberDTO.getMemberPhone());
        }
        if (updatedMemberDTO.getMemberDob() != null) {
            existingMember.setMemberDob(updatedMemberDTO.getMemberDob());
        }
        if (updatedMemberDTO.getMemberRoadAddress() != null) {
            existingMember.setMemberRoadAddress(updatedMemberDTO.getMemberRoadAddress());
        }
        if (updatedMemberDTO.getMemberDetailAddress() != null) {
            existingMember.setMemberDetailAddress(updatedMemberDTO.getMemberDetailAddress());
        }

        // 로깅: 업데이트할 내용 출력
        log.info("업데이트할 회원 정보: {}", updatedMemberDTO);

        // 기존 멤버 객체를 저장 (수정된 내용만 업데이트됨)
        Member savedMember = memberRepository.save(existingMember);

        // 로깅: 저장된 회원 정보 출력
        log.info("업데이트 완료된 회원 정보: {}", savedMember);

        // DTO로 매핑하여 반환
        return modelMapper.map(savedMember, MemberDTO.class);
    }

    @Transactional
    public String uploadProfilePicture(Long memberNo, MultipartFile file) throws IOException {
        Member member = memberRepository.findByMemberNo(memberNo);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 파일 이름이 null인지 확인
        if (file == null || file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일이 null이거나 파일 이름이 존재하지 않습니다.");
        }

        // 파일 확장자 검증
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다.");
        }

        // 파일 크기 검증
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 10MB를 초과할 수 없습니다.");
        }

        // 파일 저장 로직 (로컬 파일 시스템에 저장)
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();  // 파일명에 현재 시간을 추가하여 유니크한 이름 생성
        String filePath = "src/main/resources/static/images/" + fileName;  // 저장 경로를 src/main/resources/static/images로 변경


        // 파일 저장 (예: 파일 시스템에 저장)
        java.nio.file.Path path = java.nio.file.Paths.get(filePath);
        java.nio.file.Files.write(path, file.getBytes());

        // 기존 이미지 엔티티가 있다면 가져오고, 없으면 새로 생성
        Images image = member.getImage();
        if (image == null) {
            image = Images.builder()
                    .imageOrigName(file.getOriginalFilename())       // 원본 파일명
                    .imageSavedName(fileName)                        // 저장된 파일명
                    .imageSavedPath(filePath)                        // 저장된 파일 경로
                    .imageUrl("/images/" + fileName)                // URL 경로 (이 부분은 나중에 실제 서비스에 맞게 변경)
                    .build();
        } else {
            // 기존 이미지 엔티티가 있을 경우 업데이트
            image = Images.builder()
                    .imageNo(image.getImageNo())
                    .imageOrigName(file.getOriginalFilename())
                    .imageSavedName(fileName)
                    .imageSavedPath(filePath)
                    .imageUrl("/images/" + fileName)  // URL 경로는 실제 서비스에 맞게 수정
                    .build();
        }

        // Member 객체에 이미지 엔티티 설정
        Member updatedMember = Member.builder()
                .memberId(member.getMemberId())
                .memberPwd(member.getMemberPwd())
                .memberNickname(member.getMemberNickname())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberDob(member.getMemberDob())
                .memberRoadAddress(member.getMemberRoadAddress())
                .memberDetailAddress(member.getMemberDetailAddress())
                .image(image)  // 이미지 엔티티 설정
                .hasBusinessRegistered(member.isHasBusinessRegistered()) // 추가
                .build();

        memberRepository.save(updatedMember);

        return filePath;
    }

    public boolean checkIdDuplication(String memberId) {
        return memberRepository.existsByMemberId(memberId);
    }

    public boolean checkNicknameDuplication(String nickname) {
        return memberRepository.existsByMemberNickname(nickname);
    }

    public boolean checkEmailDuplication(String email) {
        return memberRepository.existsByMemberEmail(email);
    }

    public boolean checkPhoneDuplication(String phone) {
        return memberRepository.existsByMemberPhone(phone);
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    @Transactional
    public void changePassword(Long memberNo, ChangePasswordDTO changePasswordDTO) {
        Member member = memberRepository.findByMemberNo(memberNo);
        if (member == null) {
            throw new NoSuchElementException("회원을 찾을 수 없습니다.");
        }

        // 현재 비밀번호 확인
        if (!member.getMemberPwd().equals(changePasswordDTO.getCurrentPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 올바르지 않습니다.");
        }

        // 새 비밀번호 검증
        String newPassword = changePasswordDTO.getNewPassword();
        if (newPassword.length() < 8 || newPassword.length() > 20) {
            throw new IllegalArgumentException("새 비밀번호는 8자 이상 20자 미만이어야 합니다.");
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!newPassword.equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 변경
        Member updatedMember = Member.builder()
                .memberId(member.getMemberId())
                .memberPwd(newPassword) // 새로운 비밀번호 설정
                .memberNickname(member.getMemberNickname())
                .memberName(member.getMemberName())
                .memberEmail(member.getMemberEmail())
                .memberPhone(member.getMemberPhone())
                .memberDob(member.getMemberDob())
                .memberRoadAddress(member.getMemberRoadAddress())
                .memberDetailAddress(member.getMemberDetailAddress())
                .image(member.getImage())
                .hasBusinessRegistered(member.isHasBusinessRegistered()) // 추가
                .build();

        memberRepository.save(updatedMember);
    }

    // 여기서부터 post

    @Transactional
    public PostDTO registerBusiness(Long memberNo, PostDTO postDTO, List<MultipartFile> images) throws IOException {
        Member member = memberRepository.findPostByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        if (images.size() > 10) {
            throw new IllegalArgumentException("최대 10장의 이미지만 업로드할 수 있습니다.");
        }

        Post post = modelMapper.map(postDTO, Post.class);
        post.setMember(member);


        List<Images> postImages = uploadImages(images);
//        post.setImages(postImages);

        Post savedPost = postRepository.save(post);

        member.setHasBusinessRegistered(true);
        memberRepository.save(member);

        return modelMapper.map(savedPost, PostDTO.class);
    }

    public List<PostDTO> getMyBusinessList(Long memberNo) {
        Member member = memberRepository.findPostByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));

        List<Post> posts = postRepository.findByMember(member);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    public PostDTO getBusinessDetail(Long postNo, Long memberNo) {
        Post post = (Post) postRepository.findByPostNoAndMemberMemberNo(postNo, memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));
        return modelMapper.map(post, PostDTO.class);
    }

    @Transactional
    public PostDTO updateBusiness(Long postNo, Long memberNo, PostDTO updatedPostDTO, List<MultipartFile> newImages) throws IOException {
        Post existingPost = (Post) postRepository.findByPostNoAndMemberMemberNo(postNo, memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // DTO를 기존 게시물에 매핑
        modelMapper.map(updatedPostDTO, existingPost);

        if (newImages != null && !newImages.isEmpty()) {
            if (newImages.size() > 10) {
                throw new IllegalArgumentException("최대 10장의 이미지만 업로드할 수 있습니다.");
            }

            // 기존 이미지 삭제
            deleteExistingImages(existingPost.getPostImages());

            // 새 이미지 업로드
            List<Images> uploadedImages = uploadImages(newImages);
            List<PostImages> postImagesList = new ArrayList<>();

            for (Images image : uploadedImages) {
                PostImages postImage = new PostImages();
                postImage.setPost(existingPost); // 현재 게시물 설정
                postImage.setImage(image); // 업로드된 이미지 설정
                postImagesList.add(postImage);
            }

            existingPost.setPostImages(postImagesList); // 기존 게시물에 새 이미지 리스트 설정
        }

        Post savedPost = postRepository.save(existingPost); // 업데이트된 게시물 저장
        return modelMapper.map(savedPost, PostDTO.class); // 저장된 게시물 DTO 반환
    }

    private List<Images> uploadImages(List<MultipartFile> images) throws IOException {
        // 이미지 업로드 로직 구현
        // 실제 구현은 사용하는 스토리지 서비스에 따라 다를 수 있습니다.
        return null;
    }

    private void deleteExistingImages(List<PostImages> images) {
        // 기존 이미지 삭제 로직 구현
        // 실제 구현은 사용하는 스토리지 서비스에 따라 다를 수 있습니다.
    }

    @Transactional
    public void deleteBusiness(Long postNo, Long memberNo) {
        // 게시물과 회원 번호로 게시물을 조회
        Post post = (Post) postRepository.findByPostNoAndMemberMemberNo(postNo, memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당 게시물을 찾을 수 없습니다."));

        // 게시물 삭제
        postRepository.delete(post);
    }


}