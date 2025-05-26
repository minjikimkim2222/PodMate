package com.podmate.domain.notification.application;

import com.podmate.domain.notification.api.NotificationController;
import com.podmate.domain.notification.converter.NotificationConverter;
import com.podmate.domain.notification.domain.NotificationRepository;
import com.podmate.domain.notification.domain.dto.NotificationResponseDto;
import com.podmate.domain.notification.domain.entity.Notification;
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

import static com.podmate.domain.notification.domain.enums.NotificationType.PARTICIPATION_REQUEST;
import static com.podmate.domain.notification.domain.enums.NotificationType.RECRUITMENT_DONE;

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

        if (NotificationController.sseEmitters.containsKey(receiverId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiverId);
            try {
                String content = requester.getNickname() + " 님이 " + pod.getPodName() + " 팟에 신청하였습니다.";
                // DB 저장
                Notification notification = notificationConverter.toEntity(receiver, content, PARTICIPATION_REQUEST);
                notificationRepository.save(notification);

                String relatedUrl = "/api/mypage/mypods/"+pod.getId().toString()+"/"+receiver.getId()+"/order";

                // 응답 DTO 구성
                NotificationResponseDto.NotificationDto notificationDto = notificationConverter.toDto(notification, pod.getId(), receiver.getId(), relatedUrl);
                sseEmitter.send(SseEmitter.event()
                        .name("requestParticipation")
                        .data(notificationDto));

                // 알림 개수 증가
                notificationCounts.put(receiverId, notificationCounts.getOrDefault(receiverId, 0) + 1);

                // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(receiverId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(receiverId);
            }
        }
    }

    public void markAsRead(Long userId, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndReceiverId(notificationId, userId)
                .orElseThrow(() -> new NotificationNotFoundException());

        if (!notification.isRead()) {
            notification.markAsRead();
        }
    }

    public Integer getUnreadCount(Long receiverId) {
        Integer count = Math.toIntExact(notificationRepository.countByReceiver_IdAndIsReadFalse(receiverId));
        return count;
    }

    public void notifyRecruitmentDone(Long receiverId, Pod pod) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new UserNotFoundException());

        if (NotificationController.sseEmitters.containsKey(receiverId)) {
            SseEmitter sseEmitter = NotificationController.sseEmitters.get(receiverId);
            try {
                String content = pod.getPodName() + " 팟의 모집이 완료되었습니다.";
                // DB 저장
                Notification notification = notificationConverter.toEntity(receiver, content, RECRUITMENT_DONE);
                notificationRepository.save(notification);

                String relatedUrl = "/api/mypage/inprogress/mypods";

                // 응답 DTO 구성
                NotificationResponseDto.NotificationDto notificationDto = notificationConverter.toDto(notification, pod.getId(), receiver.getId(), relatedUrl);
                sseEmitter.send(SseEmitter.event()
                        .name("recruitmentDone")
                        .data(notificationDto));

                // 알림 개수 증가
                notificationCounts.put(receiverId, notificationCounts.getOrDefault(receiverId, 0) + 1);

                // 현재 알림 개수 전송
                sseEmitter.send(SseEmitter.event().name("notificationCount").data(notificationCounts.get(receiverId)));

            } catch (IOException e) {
                NotificationController.sseEmitters.remove(receiverId);
            }
        }
    }
}
