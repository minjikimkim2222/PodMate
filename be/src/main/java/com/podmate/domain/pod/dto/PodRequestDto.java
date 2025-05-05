package com.podmate.domain.pod.dto;

import com.podmate.domain.pod.domain.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class PodRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MininumRequestDto{
        private String podName;
        private String platform;
        private Long addressId;
        private LocalDate deadline;
        private int goalAmount;     //목표 금액 or 모집 인원
        private String description;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GroupBuyRequestDto{
        private String podName;
        private Long addressId;
        private LocalDate deadline;
        private int goalAmount;     //목표 금액 or 모집 인원
        private String description;
        private int totalAmount;  //전체 금액이 공동구매 유형에서 필요한가
        private String itemUrl;
        private int unitQuantity;
        private int unitPrice;
    }
}
