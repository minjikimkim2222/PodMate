package com.podmate.domain.token.exception;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class TokenUnauthorizedException extends GeneralException {

    public TokenUnauthorizedException() {
        super(ErrorStatus.TOKEN_UNAUTHORIZED);
    }

}
