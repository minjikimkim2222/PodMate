package com.podmate.domain.token.application;

import com.podmate.domain.token.dto.TokenResponseDto;

public interface TokenService {

    TokenResponseDto.AuthenticationResponse reissueToken(String refreshToken);

    TokenResponseDto.TokenInfo getTokenInfo(Long refreshTokenId);
}
