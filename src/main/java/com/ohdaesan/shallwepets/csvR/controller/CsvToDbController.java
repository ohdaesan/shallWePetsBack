package com.ohdaesan.shallwepets.csvR.controller;

import com.ohdaesan.shallwepets.member.domain.entity.Member;
import com.ohdaesan.shallwepets.member.repository.MemberRepository;
import com.ohdaesan.shallwepets.post.domain.dto.PostDTO;
import com.ohdaesan.shallwepets.post.domain.entity.Post;
import com.ohdaesan.shallwepets.post.domain.entity.Status;
import com.ohdaesan.shallwepets.post.repository.PostRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
public class CsvToDbController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/import-csv")
    public String importCsvToDb() {
        String csvFilePath = "src/main/resources/static/post.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm");

        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            String[] nextLine;
            boolean isFirstLine = true; // 첫 번째 줄(헤더)인지 체크

            int temp = 0;
            while ((nextLine = csvReader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // 첫 번째 줄은 헤더이므로 건너뜁니다.
                    continue;
                }

                // CSV의 각 열을 DTO 객체로 매핑
                PostDTO dto = new PostDTO();
                dto.setFcltyNm(nextLine[0]);
                dto.setCtgryTwoNm(nextLine[1]);
                dto.setCtgryThreeNm(nextLine[2]);
                dto.setCtyprvnNm(nextLine[3]);
                dto.setSignguNm(nextLine[4]);
                dto.setLegalDongNm(nextLine[5]);
                dto.setLiNm(nextLine[6]);
                dto.setLnbrNm(nextLine[7]);
                dto.setRoadNm(nextLine[8]);
                dto.setBuldNo(nextLine[9]);
                dto.setLcLa(nextLine[10]);
                dto.setLcLo(nextLine[11]);
                dto.setZipNo(nextLine[12]);
                dto.setRdnmadrNm(nextLine[13]);
                dto.setLnmAddr(nextLine[14]);
                dto.setTelNo(nextLine[15]);
                dto.setHmpgUrl(nextLine[16]);
                dto.setRstdeGuidCn(nextLine[17]);
                dto.setOperTime(nextLine[18]);
                dto.setParkngPosblAt(nextLine[19]);
                dto.setUtilizaPrcCn(nextLine[20]);
                dto.setPetPosblAt(nextLine[21]);
                dto.setEntrnPosblPetSizeValue(nextLine[22]);
                dto.setPetLmttMtrCn(nextLine[23]);
                dto.setInPlaceAcpPosblAt(nextLine[24]);
                dto.setOutPlaceAcpPosblAt(nextLine[25]);
                dto.setPetAcpAditChrgeValue(nextLine[27]);


                System.out.println("dto:" + dto);

                // member_no 설정 부분
                String memberNoStr = nextLine[28]; // CSV에서 member_no 가져오기
                if (memberNoStr == null || memberNoStr.trim().isEmpty()) {
                    System.out.println("member_no 값이 비어있습니다.");
                    continue; // 잘못된 값이 있을 경우 해당 라인을 건너뛰고 다음 라인으로 진행
                }

                // member_no는 Long으로 변환
                Long memberNo;
                try {
                    memberNo = Long.valueOf(memberNoStr);
                } catch (NumberFormatException e) {
                    System.out.println("유효하지 않은 member_no 값: " + memberNoStr);
                    continue; // 잘못된 값이 있을 경우 해당 라인을 건너뛰고 다음 라인으로 진행
                }

                // 연관된 Member 찾아오기
                Member member = memberRepository.findById(memberNo).orElse(null);
                if (member == null) {
                    System.out.println("유효하지 않은 member_no: " + memberNo);
                    continue; // 잘못된 값이 있을 경우 해당 라인을 건너뛰고 다음 라인으로 진행
                }

                // created_date 파싱
                try {
                    dto.setCreatedDate(LocalDateTime.parse(nextLine[29], formatter)); // created_date 파싱
                } catch (DateTimeParseException e) {
                    System.out.println("유효하지 않은 created_date 값: " + nextLine[29]);
                    continue; // 잘못된 값이 있을 경우 해당 라인을 건너뛰고 다음 라인으로 진행
                }


                dto.setStatusExplanation(nextLine[31]);
                dto.setViewCount(0); // view_count는 int로 변환


                // DTO에서 Entity로 변환
                Post post = Post.builder()
                        .fcltyNm(dto.getFcltyNm())
                        .ctgryTwoNm(dto.getCtgryTwoNm())
                        .ctgryThreeNm(dto.getCtgryThreeNm())
                        .ctyprvnNm(dto.getCtyprvnNm())
                        .signguNm(dto.getSignguNm())
                        .legalDongNm(dto.getLegalDongNm())
                        .liNm(dto.getLiNm())
                        .lnbrNm(dto.getLnbrNm())
                        .roadNm(dto.getRoadNm())
                        .buldNo(dto.getBuldNo())
                        .lcLa(dto.getLcLa())
                        .lcLo(dto.getLcLo())
                        .zipNo(dto.getZipNo())
                        .rdnmadrNm(dto.getRdnmadrNm())
                        .lnmAddr(dto.getLnmAddr())
                        .telNo(dto.getTelNo())
                        .hmpgUrl(dto.getHmpgUrl())
                        .rstdeGuidCn(dto.getRstdeGuidCn())
                        .operTime(dto.getOperTime())
                        .parkngPosblAt(dto.getParkngPosblAt())
                        .utilizaPrcCn(dto.getUtilizaPrcCn())
                        .petPosblAt(dto.getPetPosblAt())
                        .entrnPosblPetSizeValue(dto.getEntrnPosblPetSizeValue())
                        .petLmttMtrCn(dto.getPetLmttMtrCn())
                        .inPlaceAcpPosblAt(dto.getInPlaceAcpPosblAt())
                        .outPlaceAcpPosblAt(dto.getOutPlaceAcpPosblAt())
                        .fcltyInfoDc(dto.getFcltyInfoDc())
                        .petAcpAditChrgeValue(dto.getPetAcpAditChrgeValue())
                        .viewCount(dto.getViewCount())
                        .statusExplanation(dto.getStatusExplanation())
                        .createdDate(dto.getCreatedDate())
                        .status(Status.APPROVED) // 기본 상태는 Awaiting
                        .member(member)
                        .build();

                // 데이터베이스에 삽입
                postRepository.save(post);

                System.out.println("temp:" + ++temp);

                System.out.println("데이터 완료!");
            }
            return "CSV 데이터 삽입 완료!";
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return "CSV 삽입 중 오류 발생";
        }
    }
}
