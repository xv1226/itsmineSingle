package com.sparta.itsminesingle.global.exception.user;


import com.sparta.itsminesingle.global.common.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ResponseExceptionEnum responseExceptionEnum;

    public UserException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }

}