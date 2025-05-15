package com.podmate.domain.delivery.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class DeliveryNotFoundException extends GeneralException {
    public DeliveryNotFoundException() {
        super(ErrorStatus.DELIVERY_NOT_FOUND);
    }
}
