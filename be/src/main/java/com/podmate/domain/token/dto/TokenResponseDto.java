package com.podmate.domain.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthenticationResponse {
        private String accessToken;
        private String refreshToken;

    }

    @Builder
    @Getter
    public static class TokenInfo{
        private Long userId;
        private String refreshToken;
    }
}
