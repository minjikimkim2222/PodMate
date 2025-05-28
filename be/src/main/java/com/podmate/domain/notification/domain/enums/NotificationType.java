package com.podmate.domain.notification.domain.enums;

public enum NotificationType {
    // 공통
    REVIEW_REQUEST,          // 거래 후기 요청

    // 팟장 관련
    PARTICIPATION_REQUEST,   // 참여 요청 도착
    RECRUITMENT_DONE,        // 모집 완료
    PAYMENT_COMPLETED,       // 팟장들의 입금 완료
    ADD_TRACKING_NUM,        // 운송장 입력 요청

    // 팟원 관련
    PARTICIPATION_APPROVED,  // 팟 참여 승인
    PARTICIPATION_REJECTED,  // 팟 참여 거절
    PAYMENT_REQUEST,         // 입금 요청
    ORDER_PLACED,            // 주문 완료
    DELIVERY_STARTED,        // 배송 시작
    DELIVERY_ARRIVED,        // 물품 도착
}
