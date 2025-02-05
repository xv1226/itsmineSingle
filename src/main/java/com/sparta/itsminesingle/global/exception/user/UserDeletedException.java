package com.sparta.itsminesingle.global.exception.user;

import com.sparta.itsminesingle.global.common.response.ResponseExceptionEnum;

public class UserDeletedException extends UserException{

	public UserDeletedException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
