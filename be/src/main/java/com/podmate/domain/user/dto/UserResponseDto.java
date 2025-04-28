package com.podmate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInfo{
        private Long userId;
        private String role;
    }


    @Getter
    @AllArgsConstructor
    public static class AddressInfo {
        private Long userId;
        private Long addressId;
    }
}
