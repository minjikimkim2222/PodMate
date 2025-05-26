package com.podmate.domain.notification.application;

import com.podmate.domain.notification.api.NotificationController;
import com.podmate.domain.notification.converter.NotificationConverter;
import com.podmate.domain.notification.domain.NotificationRepository;
import com.podmate.domain.notification.domain.dto.NotificationResponseDto;
import com.podmate.domain.notification.domain.entity.Notification;
import com.podmate.domain.notification.domain.enums.NotificationType;
import com.podmate.domain.notification.exception.NotificationNotFoundException;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.domain.repository.PodRepository;
import com.podmate.domain.pod.exception.PodNotFoundException;
import com.podmate.domain.podUserMapping.domain.repository.PodUserMappingRepository;
import com.podmate.domain.podUserMapping.exception.PodLeaderNotFoundException;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.podmate.domain.notification.domain.enums.NotificationType.*;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final UserRepository userRepository;
    private final PodRepository podRepository;
    private final PodUserMappingRepository podUserMappingRepository;
    private final NotificationRepository notificationRepository;
    private static Map<Long, Integer> notificationCounts = new HashMap<>(); //알림 개수 저장
    private final NotificationConverter notificationConverter;

    // 메시지 알림
    public SseEmitter subscribe(Long userId) {

        // 현재 클라이언트를 위한 sseEmitter 객체 생성
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);

        // 연결
        try {
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // user 의 pk 값을 key 값으로 해서 sseEmitter 를 저장
        NotificationController.sseEmitters.put(userId, sseEmitter);

        // 연결 종료 처리
        sseEmitter.onCompletion(() -> NotificationController.sseEmitters.remove(userId));	// sseEmitter 연결이 완료될 경우
        sseEmitter.onTimeout(() -> NotificationController.sseEmitters.remove(userId));		// sseEmitter 연결에 타임아웃이 발생할 경우
        sseEmitter.onError((e) -> NotificationController.sseEmitters.remove(userId));		// sseEmitter 연결에 오류가 발생할 경우

        return sseEmitter;
    }

    // 팟원의 참여 요청 알림
    public void notifyParticipationRequest(Long requesterId, Long podId){
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new UserNotFoundException());
        Pod pod = podRepository.findById(podId)
                .orElseThrow(() -> new PodNotFoundException());

        Long receiverId = podUserMappingRepository.findLeaderUserIdByPodId(pod.getId())
                .orElseThrow(()-> new PodLeaderNotFoundException());
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException());

        String content = requester.getNickname() + " 님이 " + pod.getPodName() + " 팟에 신청하였습니다.";
        String relatedUrl = "/api/mypage/mypods/"+pod.getId().toString()+"/"+receiver.getId()+"/order";

        sendNotification(receiver, content, PARTICIPATION_REQUEST, "requestParticipation", relatedUrl);
    }

    // 알림 읽음 처리
    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new NotificationNotFoundException());

        if (!notification.isRead()) {
            notification.markAsRead();
        }
    }

    // 읽지 않은 알림 개수
    public Integer getUnreadCount(Long receiverId) {
        Integer count = Math.toIntExact(notificationRepository.countByReceiver_IdAndIsReadFalse(receiverId));
        return count;
    }

    // 팟의 모집 완료 알림
    public void notifyRecruitmentDone(Long receiverId, Pod pod) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException());

        String content = pod.getPodName() + " 팟의 모집이 완료되었습니다.";
        String relatedUrl = "/api/mypage/inprogress/mypods";

        sendNotification(receiver, content, RECRUITMENT_DONE, "recruitmentDone", relatedUrl);
    }

    // 팟원의 신청 수락
    public void notifyParticipationApproved(Long userId, Pod pod) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        String content = pod.getPodName() + " 팟의 참여가 승인되었습니다.";
        String relatedUrl = "/api/mypage/inprogress/joinedpods/" + pod.getId();

        sendNotification(receiver, content, PARTICIPATION_APPROVED, "participationApproved", relatedUrl);
    }

    // 팟원의 신청 거절
    public void notifyParticipationRejected(Long userId, Pod pod) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        String content = pod.getPodName() + " 팟의 참여가 거절되었습니다.";
        sendNotification(receiver, content, PARTICIPATION_REJECTED, "participationRejected", null);
    }

    public void notifyReviewRequest(Long userId, Pod pod) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        String content = pod.getPodName() + " 팟에 거래 후기를 남겨주세요.";
        String relatedUrl = "/api/reviews/" + pod.getId();

        sendNotification(receiver, content, REVIEW_REQUEST, "reviewRequest", relatedUrl);
    }

    // 주요 알림 기능
    public void sendNotification(User receiver, String content, NotificationType type, String eventName, String relatedUrl) {
        if (!NotificationController.sseEmitters.containsKey(receiver.getId())) return;

        SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiver.getId());
        try {
            // DB 저장
            Notification notification = notificationConverter.toEntity(receiver, content, type);
            notificationRepository.save(notification);

            // DTO 구성
            NotificationResponseDto.NotificationDto notificationDto = notificationConverter.toDto(notification, relatedUrl);

            // 알림 전송
            sseEmitter.send(SseEmitter.event().name(eventName).data(notificationDto));

            // 알림 개수 관리 및 전송
            notificationCounts.put(receiver.getId(), notificationCounts.getOrDefault(receiver.getId(), 0) + 1);
            sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(receiver.getId())));
        } catch (IOException e) {
            NotificationController.sseEmitters.remove(receiver.getId());
        }
    }
}
