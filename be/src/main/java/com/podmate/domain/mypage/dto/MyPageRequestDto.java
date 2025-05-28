package com.podmate.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
public class MyPageRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TrackingNumRequestDto{
        private String trackingNum;     //운송장 번호
        private String courierCompany;  //택배사
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IsApprovedStatusRequestDto{
        private String isApprovedStatus;     //팟장의 팟원 수락 상태
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DepositAccountRequestDto{
        private String depositAccountBank;     // 은행명
        private String depositAccountNumber;   // 계좌번호
        private String depositAccountHolder;   // 예금주
    }
}