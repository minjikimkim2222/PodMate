package com.podmate.global.exception;


import com.podmate.global.common.code.BaseErrorCode;

public abstract class GeneralException extends RuntimeException{
    private final BaseErrorCode errorCode;

    protected GeneralException(BaseErrorCode errorCode){
        super(errorCode.getReasonHttpStatus().getMessage());
        this.errorCode = errorCode;
    }

    public BaseErrorCode getErrorCode(){
        return errorCode;
    }

}
