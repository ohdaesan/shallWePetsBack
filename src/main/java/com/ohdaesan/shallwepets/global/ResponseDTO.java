package com.ohdaesan.shallwepets.global;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResponseDTO {
    private int httpStatusCode;
    private String message;
    private Object results;

    public ResponseDTO(HttpStatus status, String message, Object data) {
        super();
        this.httpStatusCode = status.value();		// HttpStatus enum 타입에서 value라는 int형 상태 코드 값만 추출
        this.message = message;
        this.results = data;
    }
}
