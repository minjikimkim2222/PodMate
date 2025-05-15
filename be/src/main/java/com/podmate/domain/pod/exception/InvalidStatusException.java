package com.podmate.domain.pod.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class InvalidStatusException extends GeneralException {
    public InvalidStatusException() {
        super(ErrorStatus.INVALID_STATUS_EXCEPTION);
    }
}
