package com.podmate.domain.review.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class ReviewOptionNotFoundException extends GeneralException {
    public ReviewOptionNotFoundException() {
        super(ErrorStatus.REVIEW_OPTION_NOT_FOUND);
    }
}
