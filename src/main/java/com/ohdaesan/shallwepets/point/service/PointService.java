package com.ohdaesan.shallwepets.point.service;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.point.domain.dto.PointDTO;
import com.ohdaesan.shallwepets.point.domain.entity.Point;
import com.ohdaesan.shallwepets.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;


    // 포인트 내역
    @Transactional(readOnly = true)
    public List<PointDTO> getPointsByMemberNo(Long memberNo) {
        // Retrieve the Member entity by memberNo
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // Retrieve points associated with the specified Member
        List<Point> points = pointRepository.findByMember(member);
        // Map Point entities to PointDTOs
        return points.stream()
                .map(point -> new PointDTO(
                        point.getPointNo(),
                        point.getMember().getMemberNo(),
                        point.getPoint(),
                        point.getCreatedDate(),
                        point.getComment()))
                .collect(Collectors.toList());

    }

    // 포인트 총합 조회
        @Transactional(readOnly = true)
        public int getTotalPoints(Long memberNo) {
            // Fetch the member using memberNo
            Member member = memberRepository.findById(memberNo)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

            // Fetch points for the found member
            List<Point> points = pointRepository.findByMember(member);
            return points.stream().mapToInt(Point::getPoint).sum();
        }

        // 포인트 추가(+-)
        @Transactional
        public PointDTO addPoints(PointDTO pointDTO) {
            // Find the member using the memberNo
            Member member = memberRepository.findById(pointDTO.getMemberNo())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

            // Create a new Point entry
            Point point = Point.builder()
                    .member(member) // Set the member entity
                    .point(pointDTO.getPoint()) // Get points from DTO
                    .createdDate(LocalDateTime.now())
                    .comment(pointDTO.getComment()) // Get comment from DTO
                    .build();

            // Save the point entry to the database
            point = pointRepository.save(point);

            // Convert Point to PointDTO for returning
            return new PointDTO(point.getPointNo(), pointDTO.getMemberNo(), point.getPoint(), point.getCreatedDate(), point.getComment());
        }
    }
