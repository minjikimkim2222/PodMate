package com.podmate.domain.platformInfo.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PlatformAlreadyExistsException extends GeneralException{

    public PlatformAlreadyExistsException() {
        super(ErrorStatus.PLATFORM_ALREADY_EXISTS_EXCEPTION);
    }

}
