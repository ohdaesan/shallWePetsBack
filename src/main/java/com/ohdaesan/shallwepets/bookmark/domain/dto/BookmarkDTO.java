package com.ohdaesan.shallwepets.bookmark.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BookmarkDTO {
    private Long bookmarkNo;
    private Long memberNo;
    private Long postNo;
    private LocalDateTime createdDate;
}
