package com.podmate.domain.notification.domain;

import com.podmate.domain.notification.domain.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByIdAndReceiverId(Long id, Long receiverId);

    long countByReceiver_IdAndIsReadFalse(Long receiverId);
}