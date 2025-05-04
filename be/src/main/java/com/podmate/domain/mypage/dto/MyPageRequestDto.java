package com.podmate.domain.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class MyPageRequestDto {

    private String trackingNum;     //운송장 번호
    private String courierCompany;  //택배사
}
