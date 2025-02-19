package com.sparta.itsminesingle.global.exception.user;

import com.sparta.itsminesingle.global.response.ResponseExceptionEnum;

public class UserDeletedException extends UserException{

	public UserDeletedException(ResponseExceptionEnum responseExceptionEnum) {
		super(responseExceptionEnum);
	}
}
