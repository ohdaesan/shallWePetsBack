package com.ohdaesan.shallwepets.global;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ErrorResponse {
    private String code;
    private String description;
    private String detail;
}