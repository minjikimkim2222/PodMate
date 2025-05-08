package com.podmate.domain.orderForm.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class OrderFormNotFoundException extends GeneralException {

    public OrderFormNotFoundException() {
        super(ErrorStatus.ORDERFORM_NOT_FOUND);
    }

}
