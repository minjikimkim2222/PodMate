package com.podmate.domain.pod.converter;

import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.dto.PodResponseDto;

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

}
