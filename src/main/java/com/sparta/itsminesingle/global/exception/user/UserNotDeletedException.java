package com.sparta.itsminesingle.global.exception.user;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class UserNotDeletedException extends UserException{

	public UserNotDeletedException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
