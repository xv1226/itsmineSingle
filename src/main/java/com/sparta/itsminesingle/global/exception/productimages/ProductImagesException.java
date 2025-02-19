package com.sparta.itsminesingle.global.exception.productimages;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class ProductImagesException extends RuntimeException{
    private final ResponseExceptionEnum responseExceptionEnum;
    public ProductImagesException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
