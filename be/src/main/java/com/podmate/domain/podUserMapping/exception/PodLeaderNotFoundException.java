package com.podmate.domain.podUserMapping.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodLeaderNotFoundException extends GeneralException {

    public PodLeaderNotFoundException(){
        super(ErrorStatus.POD_LEADER_NOT_FOUND);
    }
}
