package com.podmate.domain.user.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class UserNotFoundException extends GeneralException {

    public UserNotFoundException() {
        super(ErrorStatus.USER_NOT_FOUND);
    }

}
