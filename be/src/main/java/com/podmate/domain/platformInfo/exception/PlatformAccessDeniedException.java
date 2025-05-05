package com.podmate.domain.platformInfo.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PlatformAccessDeniedException extends GeneralException {
    public PlatformAccessDeniedException(){
        super(ErrorStatus.PLATFORM_ACCESS_DENIED_EXCEPTION);
    }
}
