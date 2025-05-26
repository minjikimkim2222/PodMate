package com.podmate.domain.notification.exception;

import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.exception.GeneralException;

public class NotificationNotFoundException extends GeneralException {
    public NotificationNotFoundException() {
        super(ErrorStatus.NOTIFICATION_NOT_FOUND);
    }
}
