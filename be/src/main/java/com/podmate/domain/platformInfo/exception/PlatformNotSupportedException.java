package com.podmate.domain.platformInfo.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PlatformNotSupportedException extends GeneralException {
    public PlatformNotSupportedException(){
        super(ErrorStatus.PLATFORM_NOT_SUPPORTED);
    }
}
