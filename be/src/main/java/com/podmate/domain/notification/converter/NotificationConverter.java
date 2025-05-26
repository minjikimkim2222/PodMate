package com.podmate.domain.notification.converter;

import com.podmate.domain.notification.domain.dto.NotificationResponseDto;
import com.podmate.domain.notification.domain.entity.Notification;
import com.podmate.domain.notification.domain.enums.NotificationType;
import com.podmate.domain.user.domain.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationConverter {
    public Notification toEntity(User receiver, String content, NotificationType type) {
        return Notification.builder()
                .receiver(receiver)
                .notificationType(type)
                .content(content)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public NotificationResponseDto.NotificationDto toDto(Notification notification, Long podId, Long receiverId, String relatedUrl) {
        return NotificationResponseDto.NotificationDto.builder()
                .imageUrl(notification.getReceiver().getProfileImage())
                .noticeType(notification.getNotificationType().toString())
                .content(notification.getContent())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .relatedUrl(relatedUrl)
                .build();
    }
}
