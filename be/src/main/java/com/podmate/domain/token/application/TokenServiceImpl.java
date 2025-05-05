package com.podmate.domain.token.application;

import com.podmate.domain.token.converter.TokenConverter;
import com.podmate.domain.token.domain.entity.Token;
import com.podmate.domain.token.domain.repository.TokenRepository;
import com.podmate.domain.token.dto.TokenResponseDto;
import com.podmate.domain.token.dto.TokenResponseDto.AuthenticationResponse;
import com.podmate.domain.token.dto.TokenResponseDto.TokenInfo;
import com.podmate.domain.token.exception.RefreshTokenNotFoundException;
import com.podmate.domain.token.exception.TokenUnauthorizedException;
import com.podmate.global.util.jwt.JwtUtil;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    @Override
    public AuthenticationResponse reissueToken(String refreshToken) {
        Long userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 1. refreshToken 검증
        Token token = tokenRepository.findByUserId(userId)
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (!token.getRefreshToken().equals(refreshToken) || jwtUtil.isExpired(refreshToken)) {
            throw new TokenUnauthorizedException();
        }

        // 2. 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken("accessToken", userId, role, 30 * 60 * 1000L);
        String newRefreshToken = jwtUtil.createRefreshToken("refreshToken", userId, 30 * 24 * 60 * 60 * 1000L);

        // 3. DB 갱신
        tokenRepository.delete(token);
        tokenRepository.flush(); // 즉시 delete 쿼리 날림 -- 안 그러면 유저당 토큰 1개인 것이 유지되지 않아서, 예외가 발생했음..
        log.info("TokenServiceImpl -- token delete 완료");
        tokenRepository.save(Token.toEntity(token.getUser(), newRefreshToken, LocalDateTime.now().plusDays(30)));
        log.info("TokenServiceImpl -- token save 완료");

        return TokenResponseDto.AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public TokenInfo getTokenInfo(Long refreshTokenId) {

        Token token = tokenRepository.findById(refreshTokenId)
                .orElseThrow(() -> new RefreshTokenNotFoundException());

        return TokenConverter.toTokenInfo(token);
    }

}
