package com.sparta.itsminesingle.global.exception.comment;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class CommentEqualSellerException extends CommentException{
    public CommentEqualSellerException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
