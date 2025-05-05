package com.podmate.domain.podUserMapping.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodLeaderUserMismatchException extends GeneralException {
    public PodLeaderUserMismatchException() {
        super(ErrorStatus.POD_LEADER_USER_MISMATCH);
    }
}
