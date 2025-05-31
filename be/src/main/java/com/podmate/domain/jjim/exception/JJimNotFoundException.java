package com.podmate.domain.jjim.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class JJimNotFoundException extends GeneralException {
    public JJimNotFoundException() {
        super(ErrorStatus.JJIM_NOT_FOUND);
    }
}
