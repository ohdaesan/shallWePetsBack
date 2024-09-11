package com.ohdaesan.shallwepets.common;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseMsg {
    private int httpStatusCode;
    private String message;
    private Map<String, Object> results;
}
