package com.sparta.itsminesingle.global.exception;

import static com.sparta.itsminesingle.global.response.ResponseUtils.of;

import com.sparta.itsminesingle.global.response.HttpResponseDto;
import com.sparta.itsminesingle.global.exception.product.ProductException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    // 공통된 오류 처리 로직
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(CommonException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<HttpResponseDto> handlerProductException(ProductException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(DataNotFoundException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }

    @ExceptionHandler(DataDuplicatedException.class)
    public ResponseEntity<HttpResponseDto> handleUserException(DataDuplicatedException e) {
        log.error("에러 메세지: ", e);
        return of(e.getResponseExceptionEnum());
    }
}