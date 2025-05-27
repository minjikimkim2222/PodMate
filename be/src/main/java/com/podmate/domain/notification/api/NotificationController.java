package com.podmate.domain.notification.api;

import com.podmate.domain.notification.application.NotificationService;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // 메시지 알림
    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        SseEmitter sseEmitter = notificationService.subscribe(customOAuth2User.getUserId());

        return sseEmitter;
    }
    // 알림 읽음
    @PatchMapping("/read/{id}")
    public BaseResponse<String> readNotification(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        notificationService.markAsRead(customOAuth2User.getUserId(), id);
        return BaseResponse.onSuccess(SuccessStatus._OK, "읽음 처리 완료");
    }
    // 읽지 않은 알림 개수 반환
    @GetMapping("/unread-count")
    public BaseResponse<Integer> getUnreadCount(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        Integer count = notificationService.getUnreadCount(customOAuth2User.getUserId());
        return BaseResponse.onSuccess(SuccessStatus._OK, count);
    }
}
