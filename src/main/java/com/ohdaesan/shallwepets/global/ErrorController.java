package com.ohdaesan.shallwepets.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserFindException(PostNotFoundException e){
        String code = "ERROR_CODE_0001";
        String description = "존재하지 않는 게시글입니다.";
        String detail = e.getMessage();

        return new ResponseEntity<>(
                new ErrorResponse(code, description, detail),
                HttpStatus.NOT_FOUND
        );
    }
}
