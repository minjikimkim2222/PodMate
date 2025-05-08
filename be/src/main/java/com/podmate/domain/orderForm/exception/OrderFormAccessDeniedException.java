package com.podmate.domain.orderForm.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class OrderFormAccessDeniedException extends GeneralException {

    public OrderFormAccessDeniedException() {
        super(ErrorStatus.ORDERFORM_ACCESS_DENIED_EXCEPTION);
    }

}
