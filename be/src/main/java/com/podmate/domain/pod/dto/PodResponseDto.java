package com.podmate.domain.pod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public static class GroupBuy implements PodResponse{
        private Long podId;
        private String podName;
        private String podType;
        private String itemUrl;
        private int goalAmount;     //목표 금액 or 목표 인원
        private int currentAmount;  //현재 금액 or 현재 인원
        private boolean isJJim;
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
    public static class PodLeader implements PodResponse{
        private String nickname;
        private String profileImageUrl;
        private String description;
    }
}
