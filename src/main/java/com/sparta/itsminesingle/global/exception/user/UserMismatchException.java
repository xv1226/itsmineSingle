package com.sparta.itsminesingle.global.exception.user;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class UserMismatchException extends UserException {

	public UserMismatchException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
