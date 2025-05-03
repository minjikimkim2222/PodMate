package com.podmate.domain.pod.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodNotFoundException extends GeneralException {

    public PodNotFoundException() {
        super(ErrorStatus.POD_NOT_FOUND);
    }
}
