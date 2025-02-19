package com.sparta.itsminesingle.global.exception;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class DataFormatException extends CommonException {

    public DataFormatException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
