package com.sparta.itsminesingle.global.exception;


import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class DataNotFoundException extends CommonException {

    public DataNotFoundException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
