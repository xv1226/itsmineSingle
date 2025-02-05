package com.sparta.itsminesingle.global.exception;


import com.sparta.itsminesingle.global.common.response.ResponseExceptionEnum;

public class DataNotFoundException extends CommonException {

    public DataNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
