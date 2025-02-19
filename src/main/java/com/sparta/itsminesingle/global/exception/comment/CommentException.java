package com.sparta.itsminesingle.global.exception.comment;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import lombok.Getter;

@Getter
public class CommentException extends RuntimeException{
    private final ResponseExceptionEnum responseExceptionEnum;
    public CommentException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum.getMessage());
        this.responseExceptionEnum = responseExceptionEnum;
    }
}
