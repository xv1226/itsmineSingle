package com.sparta.itsminesingle.global.exception.comment;

import com.sparta.itsminesingle.global.common.response.ResponseExceptionEnum;

public class CommentEqualSellerException extends CommentException{
    public CommentEqualSellerException(ResponseExceptionEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
