package com.sparta.itsminesingle.global.exception.product;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;
import com.sparta.itsminesingle.global.exception.CommonException;

public class ProductInDateException extends CommonException {

    public ProductInDateException(ResponseExceptionEnum responseExceptionEnum) {
        super(responseExceptionEnum);
    }
}
