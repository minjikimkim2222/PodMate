package com.podmate.domain.orderForm.dto;

import com.podmate.domain.pod.domain.enums.Platform;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
public class OrderFormResponseDto {
    @Getter
    @AllArgsConstructor
    public static class OrderFormListResponseDto {
        private List<OrderFormDto> orderFormDtos;

        @Getter
        @AllArgsConstructor
        public static class OrderFormDto {
            private Long orderformId;
            private Platform platform;
            private int totalAmount;
        }
    }

}
