package com.ohdaesan.shallwepets.images.service;

import com.ohdaesan.shallwepets.images.domain.dto.ImagesDTO;
import com.ohdaesan.shallwepets.images.domain.entity.Images;
import com.ohdaesan.shallwepets.images.repository.ImagesRepository;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.PostImages;
import com.ohdaesan.shallwepets.post.repository.PostImagesRepository;
import com.ohdaesan.shallwepets.review.domain.entity.ReviewImages;
import com.ohdaesan.shallwepets.review.repository.ReviewImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagesService {
    private final ImagesRepository imagesRepository;
    private final MemberRepository memberRepository;
    private final PostImagesRepository postimagesRepository;
    private final ReviewImagesRepository reviewimagesRepository;

    private final ModelMapper modelMapper;

    public Images save(ImagesDTO imagesDTO) {
        Images image = modelMapper.map(imagesDTO, Images.class);
        imagesRepository.save(image);

        return image;
    }

    public Images findImagesByImageNo(Long imageNo) {
        Images image = imagesRepository.findImagesByImageNo(imageNo)
                .orElseThrow(() -> new NoSuchElementException("해당 이름과 번호로 가입한 회원이 존재하지 않습니다."));

        return image;
    }

    public void delete(Long imageNo) {
        imagesRepository.deleteById(imageNo);
    }

    public void clearImageReferences(Long imageNo) {
        // Member에서 refer하는 Image reference 제거
        List<Member> members = memberRepository.findByImage_ImageNo(imageNo);
        for (Member member : members) {
            member.setImage(null);
            memberRepository.save(member);
        }

        // PostImage에서 refer하는 Image reference 제거
        List<PostImages> postImages = postimagesRepository.findByImage_ImageNo(imageNo);
        postimagesRepository.deleteAll(postImages);

        // ReviewImage에서 refer하는 Image reference 제거
        List<ReviewImages> reviewImages = reviewimagesRepository.findByImage_ImageNo(imageNo);
        reviewimagesRepository.deleteAll(reviewImages);
    }

    public void updateImage(ImagesDTO imagesDTO) {
        Images image = modelMapper.map(imagesDTO, Images.class);
        imagesRepository.save(image);
    }

    public List<Images> getImagesByPostNo(Long postNo) {
        return imagesRepository.findImagesByPostNo(postNo);
    }

    public List<Images> getPostImagesByPostNo(Long postNo) {
        return imagesRepository.findPostImagesByPostNo(postNo);
    }

    public List<Images> getImagesByPostNoAndPageNo(Long postNo, int pageNo) {
        Page<Images> page = null;
        PageRequest pageRequest = PageRequest.of(pageNo, 12);

        page = imagesRepository.findImagesByPostNoAndPageNo(postNo, pageRequest);
        List<Images> images = page.getContent();

        return images;
    }
}
