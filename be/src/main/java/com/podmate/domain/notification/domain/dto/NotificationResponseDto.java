package com.podmate.domain.notification.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
public class NotificationResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class NotificationDto {
        private String imageUrl;
        private String noticeType;
        private String content;
        private boolean isRead;
        private LocalDateTime createdAt;
        private String relatedUrl;
    }
}
