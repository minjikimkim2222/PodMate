package com.podmate.domain.cart.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class CartItemNotFoundException extends GeneralException {

    public CartItemNotFoundException() {
        super(ErrorStatus.CARTITEM_NOT_FOUND);
    }

}
