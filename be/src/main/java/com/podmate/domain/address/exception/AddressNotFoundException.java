package com.podmate.domain.address.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class AddressNotFoundException extends GeneralException {
    public AddressNotFoundException() {
        super(ErrorStatus.ADDRESS_NOT_FOUND);
    }
}
