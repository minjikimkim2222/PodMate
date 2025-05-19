package com.podmate.domain.mypage.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class PodStatusNotRecruitingException extends GeneralException {
    public PodStatusNotRecruitingException() {
        super(ErrorStatus.POD_STATUS_NOT_RECRUITING);
    }
}
