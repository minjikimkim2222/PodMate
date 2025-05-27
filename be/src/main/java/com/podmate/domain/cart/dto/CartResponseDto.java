package com.podmate.domain.cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CartResponseDto {
    @Getter
    @AllArgsConstructor
    public static class PlatformList {
        private List<PlatformDto> platformDtos;
    }

    @Getter
    @AllArgsConstructor
    public static class PlatformDto {
        private Long platformInfoId;
        private String platformName;
        private String cartName;
    }

    @Getter
    @AllArgsConstructor
    public static class CartItemList {
        private List<CartItemDto> cartItemDtos;
        @Getter
        @Builder
        public static class CartItemDto {
            private Long itemId;
            private String itemName;
            private int quantity;
            private String optionText;
            private String itemUrl;
        }
    }
}
