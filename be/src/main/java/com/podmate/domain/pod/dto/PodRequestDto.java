package com.podmate.domain.pod.dto;

import com.podmate.domain.pod.domain.enums.InprogressStatus;
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
        private String description;
        private int totalAmount;    //전체 개수
        private String itemUrl;
        private int unitQuantity;   //단위 개수
        private int unitPrice;      //단위 가격
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupBuyJoinRequestDto{
        private int quantity;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangingInprogressStatus{
        private String status;
    }
}
