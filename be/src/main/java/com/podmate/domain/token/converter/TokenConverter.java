package com.podmate.domain.token.converter;

import com.podmate.domain.token.domain.entity.Token;
import com.podmate.domain.token.dto.TokenResponseDto;

public class TokenConverter {
    public static TokenResponseDto.TokenInfo toTokenInfo(Token token){
        return TokenResponseDto.TokenInfo.builder()
                .userId(token.getUser().getId())
                .refreshToken(token.getRefreshToken())
                .build();
    }

}
