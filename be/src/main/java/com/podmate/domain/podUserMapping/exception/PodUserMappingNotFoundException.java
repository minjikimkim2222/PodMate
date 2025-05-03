package com.podmate.domain.podUserMapping.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodUserMappingNotFoundException extends GeneralException {

    public PodUserMappingNotFoundException() {
        super(ErrorStatus.PODUSERMAPPING_NOT_FOUND);
    }
}
