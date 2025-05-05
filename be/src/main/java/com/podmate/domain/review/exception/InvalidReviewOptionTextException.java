package com.podmate.domain.review.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class InvalidReviewOptionTextException extends GeneralException {
    public InvalidReviewOptionTextException() {
        super(ErrorStatus.INVALID_REVIEW_OPTION_TEXT);
    }
}
