package com.podmate.domain.delivery.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class ShippingMismatchException extends GeneralException {
    public ShippingMismatchException() {
        super(ErrorStatus.SHIPPING_MISMATCH);
    }
}
