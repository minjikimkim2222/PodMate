package com.podmate.domain.cart.dto;

import java.util.List;
import lombok.AllArgsConstructor;
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
        private Long platformId;
        private String platformName;
    }
}
