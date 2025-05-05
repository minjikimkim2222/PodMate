package com.podmate.domain.pod.converter;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.user.domain.entity.User;

public class PodConverter {
    public static PodResponseDto.Minimum buildMinimumPodResponseDto(Pod pod, boolean isJJim) {
            return PodResponseDto.Minimum.builder()
                    .podId(pod.getId())
                    .podName(pod.getPodName())
                    .podType(pod.getPodType().name())
                    .platform(pod.getPlatform().name())
                    .goalAmount(pod.getGoalAmount())
                    .currentAmount(pod.getCurrentAmount())
                    .isJJim(isJJim)
                    .build();
    }

    public static PodResponseDto.GroupBuy buildGroupBuyPodResponseDto(Pod pod, boolean isJJim) {
        return PodResponseDto.GroupBuy.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .itemUrl(pod.getItemUrl())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .isJJim(isJJim)
                .build();
    }

    public static PodResponseDto.MinimumDetail buildMinimumDetailResponseDto(Pod pod, boolean isJJim, PodResponseDto.PodLeader podLeaderDto) {
        return PodResponseDto.MinimumDetail.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .platform(pod.getPlatform().name())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .isJJim(isJJim)
                .podLeader(podLeaderDto)
                .build();
    }

    public static PodResponseDto.GroupBuyDetail buildGroupBuyDetailResponseDto(Pod pod, boolean isJJim, PodResponseDto.PodLeader podLeaderDto) {
        return PodResponseDto.GroupBuyDetail.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().toString())
                .itemUrl(pod.getItemUrl())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .isJJim(isJJim)
                .unitQuantity(pod.getUnitQuantity())
                .unitPrice(pod.getUnitPrice())
                .podLeader(podLeaderDto)
                .build();
    }

    public static PodResponseDto.MinimumStatus buildMinimumStatusResponseDto(Pod pod) {
        return PodResponseDto.MinimumStatus.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .podStatus(pod.getPodStatus().toString())
                .platform(pod.getPlatform().name())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .build();
    }

    public static PodResponseDto.GroupBuyStaus buildGroupBuyStatusResponseDto(Pod pod) {
        return PodResponseDto.GroupBuyStaus.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .podStatus(pod.getPodStatus().toString())
                .itemUrl(pod.getItemUrl())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .build();
    }
    public static PodResponseDto.MinimumCompleted buildMinimumCompletedResponseDto(Pod pod) {
        return PodResponseDto.MinimumCompleted.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .podStatus(pod.getPodStatus().toString())
                .platform(pod.getPlatform().name())
                .build();
    }

    public static PodResponseDto.GroupBuyCompleted buildGroupBuyCompletedResponseDto(Pod pod) {
        return PodResponseDto.GroupBuyCompleted.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .podStatus(pod.getPodStatus().toString())
                .itemUrl(pod.getItemUrl())
                .build();
    }

    public static PodResponseDto.MinimumPodMember buildMinimumPodMemberResponseDto(User member, IsApproved isApproved) {
        return PodResponseDto.MinimumPodMember.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                //.totalAmount()  <-- orderForm의 총 주문 금액
                .mannerScore(member.getMannerScore())
                .isApproved(isApproved.name())
                .build();
    }

    public static PodResponseDto.PodMember buildPodMemberResponseDto(User member, IsApproved isApproved) {
        return PodResponseDto.PodMember.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                .mannerScore(member.getMannerScore())
                .isApproved(isApproved.name())
                .build();
    }

    public static PodResponseDto.MinimumInprogressJoined buildMinimumInprogressJoinedResponseDto(Pod pod, PodResponseDto.DeliveryDto deliveryDto) {
        return PodResponseDto.MinimumInprogressJoined.builder()
                .minimumStatus(buildMinimumStatusResponseDto(pod))
                .deliveryDto(deliveryDto)
                .build();
    }
    public static PodResponseDto.GroupBuyInprogressJoined buildGroupBuyInprogressJoinedResponseDto(Pod pod, PodResponseDto.DeliveryDto deliveryDto) {
        return PodResponseDto.GroupBuyInprogressJoined.builder()
                .groupBuyStatus(buildGroupBuyStatusResponseDto(pod))
                .deliveryDto(deliveryDto)
                .build();
    }
}
