package com.sparta.itsminesingle.global.exception.user;


import com.sparta.itsminesingle.global.common.response.ResponseExceptionEnum;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }

}