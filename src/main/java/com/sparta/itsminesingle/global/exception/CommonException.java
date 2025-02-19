package com.sparta.itsminesingle.global.exception;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public CommonException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}
