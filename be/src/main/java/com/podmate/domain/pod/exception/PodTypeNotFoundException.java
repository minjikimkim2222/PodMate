package com.podmate.domain.pod.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodTypeNotFoundException extends GeneralException {

    public PodTypeNotFoundException() {
        super(ErrorStatus.POD_TYPE_NOT_FOUND);
    }
}
