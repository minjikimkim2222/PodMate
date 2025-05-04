package com.podmate.domain.pod.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodStatusMismatchException extends GeneralException {
    public PodStatusMismatchException() {
        super(ErrorStatus.POD_STATUS_MISMATCH);
    }
}
