package com.podmate.domain.jjim.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class DuplicateJJimException extends GeneralException {
    public DuplicateJJimException() {
        super(ErrorStatus.DUPLICATE_JJIM);
    }
}
