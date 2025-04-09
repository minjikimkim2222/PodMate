package com.podmate.domain.token.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class RefreshTokenNotFoundException extends GeneralException {

    public RefreshTokenNotFoundException() {
        super(ErrorStatus.REFRESH_TOKEN_NOT_FOUND);
    }

}
