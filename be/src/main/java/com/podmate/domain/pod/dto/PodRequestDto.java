package com.podmate.domain.pod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PodRequestDto {

    private String podName;
    private String deadline;
    private int goalAmount; //목표 금액 or 모집 인원
    private String description;

    @Getter
    @Builder
    private static class MininumRequestDto{
        private String platform;
        private Long addressId;
    }

    @Getter
    @Builder
    private static class GroupBuyRequestDto{
        //private int totalAmount;  //전체 금액이 공동구매 유형에서 필요한가
        private String itemUrl;
        private int unitQuantity;
        private int unitPrice;
    }
}
