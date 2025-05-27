package com.podmate.domain.pod.converter;

import com.podmate.domain.address.domain.entity.Address;
import com.podmate.domain.orderForm.domain.entity.OrderForm;
import com.podmate.domain.pod.domain.entity.Pod;
import com.podmate.domain.pod.dto.PodResponseDto;
import com.podmate.domain.podUserMapping.domain.entity.PodUserMapping;
import com.podmate.domain.podUserMapping.domain.enums.IsApproved;
import com.podmate.domain.user.domain.entity.User;

import java.util.Optional;

import static java.util.Optional.ofNullable;

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

    public static PodResponseDto.Minimum buildMinimumPodResponseDtoForMap(Pod pod, boolean isJJim, boolean includeAddress) {
        Address address = pod.getAddress();
        return PodResponseDto.Minimum.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .platform(pod.getPlatform().name())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .isJJim(isJJim)

                // ✅ 주소 필드 세팅
                .addressId(address.getId())
                .roadAddress(address.getRoadAddress())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())

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

    public static PodResponseDto.GroupBuy buildGroupBuyPodResponseDtoForMap(Pod pod, boolean isJJim, boolean includeAddress) {
        Address address = pod.getAddress();
        return PodResponseDto.GroupBuy.builder()
                .podId(pod.getId())
                .podName(pod.getPodName())
                .podType(pod.getPodType().name())
                .itemUrl(pod.getItemUrl())
                .goalAmount(pod.getGoalAmount())
                .currentAmount(pod.getCurrentAmount())
                .isJJim(isJJim)

                // 주소 필드 세팅
                .addressId(address.getId())
                .roadAddress(address.getRoadAddress())
                .latitude(address.getLatitude())
                .longitude(address.getLongitude())

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

    public static PodResponseDto.MMinimumPodMember buildMinimumPodMemberResponseDto(User member, OrderForm orderForm, IsApproved isApproved) {
        PodResponseDto.MemberProfile memberProfile = PodResponseDto.MemberProfile.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                .mannerScore(member.getMannerScore())
                .isApproved(isApproved.toString())
                .build();
        PodResponseDto.OrderItem orderItem = PodResponseDto.OrderItem.builder()
                .orderformId(orderForm.getId())
                .totalAmount(orderForm.getTotalAmount())
                .build();

        return PodResponseDto.MMinimumPodMember.builder()
                .memberProfile(memberProfile)
                .orderItem(orderItem)
                .build();


    }

    public static PodResponseDto.PodMember buildGroupBuyPodMemberResponseDto(User member, PodUserMapping mapping) {
        PodResponseDto.MemberProfile memberProfile = PodResponseDto.MemberProfile.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileImage())
                .mannerScore(member.getMannerScore())
                .isApproved(mapping.getIsApproved().toString())
                .build();

        return PodResponseDto.PodMember.builder()
                .memberProfile(memberProfile)
                .groupBuyQuantity(Optional.ofNullable(mapping.getGroupBuyQuantity()).orElse(0))
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
