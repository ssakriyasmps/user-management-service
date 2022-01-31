package com.services.user.management.exception;

import lombok.Getter;

@Getter
public class UserNotFound extends RuntimeException{
    private ErrorCode errorCode;

    public UserNotFound(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    public UserNotFound(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
