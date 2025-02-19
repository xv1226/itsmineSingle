package com.sparta.itsminesingle.global.exception.user;


import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}