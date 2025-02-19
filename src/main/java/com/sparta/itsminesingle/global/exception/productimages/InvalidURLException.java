package com.sparta.itsminesingle.global.exception.productimages;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import com.sparta.itsminesingle.global.exception.comment.CommentException;

public class InvalidURLException extends CommentException {
    public InvalidURLException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
