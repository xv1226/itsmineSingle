package com.sparta.itsminesingle.global.exception.user;


import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class UserNotFoundException extends UserException {

    public UserNotFoundException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}