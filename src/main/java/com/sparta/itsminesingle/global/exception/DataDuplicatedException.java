package com.sparta.itsminesingle.global.exception;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class DataDuplicatedException extends CommonException {

    public DataDuplicatedException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
