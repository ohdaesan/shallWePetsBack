package com.ohdaesan.shallwepets.point.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PointDTO {
    private Long pointNo;
    private Long memberNo;
    private int point;
    private LocalDateTime createdDate;
    private String comment;
}
