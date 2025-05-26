package com.podmate.domain.notification.domain.entity;

import com.podmate.domain.notification.domain.enums.NotificationType;
import com.podmate.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notification")
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noti_id", nullable = false)
    private Long id;

    // 알림 대상
    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    // 알림 종류
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    // 알림 내용
    private String content;

    // 읽음 여부
    private boolean isRead = false;

    // 생성 시간
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Notification(User receiver, NotificationType notificationType, String content, boolean isRead, LocalDateTime createdAt) {
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public void markAsRead(){
        this.isRead = true;
    }

}
