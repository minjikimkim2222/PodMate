package com.podmate.domain.pod.dto;

import com.fasterxml.classmate.AnnotationOverrides;
import com.podmate.domain.delivery.domain.entity.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class PodResponseDto{ //두 유형을 list안에 내보내려면 공통 interface 필요

    @Builder
    @Getter
    @AllArgsConstructor
    public static class Minimum implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String platform;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
        private boolean isJJim;

        // 주소 필드 추가
        private Long addressId;
        private String roadAddress;
        private double latitude;
        private double longitude;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumDetail implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String platform;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
        private boolean isJJim;
        private PodLeader podLeader;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumStatus implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String podStatus;
        private String inprogressStatus;
        private String platform;
        private int goalAmount;
        private int currentAmount;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumCompleted implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String podStatus;
        private String platform;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumInprogressJoined implements PodResponse{
        private MinimumStatus minimumStatus;
        private DeliveryDto deliveryDto;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyInprogressJoined implements PodResponse{
        private GroupBuyStaus groupBuyStatus;
        private DeliveryDto deliveryDto;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuy implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String itemUrl;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
        private boolean isJJim;

        //  주소 필드 추가
        private Long addressId;
        private String roadAddress;
        private double latitude;
        private double longitude;

    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyDetail implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String itemUrl;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
        private boolean isJJim;
        private int unitQuantity;
        private int unitPrice;
        private PodLeader podLeader;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyStaus implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String podStatus;
        private String inprogressStatus;
        private String itemUrl;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyCompleted implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String podStatus;
        private String itemUrl;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class PodLeader implements PodResponse{
        private String nickname;
        private String profileImageUrl;
        private String description;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumPodMembers implements PodResponse{
        private Long podId;
        private List<MMinimumPodMember> podMembers;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumPodMember{
        private Long userId;
        private String nickname;
        private String profileImageUrl;
        private int totalAmount;    //orderform의 주문 금액
        private double mannerScore;
        private String isApproved;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MMinimumPodMember{
        private MemberProfile memberProfile;
        private OrderItem orderItem;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberProfile {
        private Long userId;
        private String nickname;
        private String profileImageUrl;
        private double mannerScore;
        private String isApproved;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private Long orderformId;
        private int totalAmount;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyPodMembers implements PodResponse{
        private Long podId;
        private String itemUrl;
        private List<PodMember> podMembers;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class PodMember{
        private MemberProfile memberProfile;
        private int groupBuyQuantity;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class DeliveryDto{
        private String courierCompany;
        private String trackingNum;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class MinimumPodInfo implements PodResponse{
        private Long podId;
        private PodLeader podLeader;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class GroupBuyPodInfo implements PodResponse{
        private Long podId;
        private PodLeader podLeader;
        private String itemUrl;
    }

}
