package com.podmate.domain.pod.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PendingOrderMismatchException extends GeneralException {
    public PendingOrderMismatchException() {
        super(ErrorStatus.PENDING_ORDER_MISMATCH);
    }
}
