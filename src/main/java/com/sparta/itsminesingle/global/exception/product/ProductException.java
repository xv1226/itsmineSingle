package com.sparta.itsminesingle.global.exception.product;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ProductException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public ProductException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}