package com.podmate.domain.token.api;

import com.podmate.domain.token.application.TokenService;
import com.podmate.domain.token.dto.TokenRequestDto;
import com.podmate.domain.token.dto.TokenResponseDto;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.podmate.domain.token.dto.TokenResponseDto.AuthenticationResponse;

// TODO: Controller에는 오직 Service 로직 위임만을 해야 하는데, 비즈니스 로직이 보인다. 리팩토링할 것
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh-token")
@Slf4j
public class TokenRestController {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    @PostMapping
    public BaseResponse<TokenResponseDto.AuthenticationResponse> refreshToken(@RequestBody TokenRequestDto.RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();

        // 리프레시 토큰 검증
        Long userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);

        // 리프레시 토큰 유효성 검증
        if (tokenService.validateToken(refreshToken, userId)){
            // 유효한 토큰이라면, 새로운 accessToken, refreshToken 생성
            String newAccessToken = jwtUtil.createAccessToken("accessToken", userId, role, 30 * 60 * 1000L);
            String newRefreshToken = jwtUtil.createRefreshToken("refreshToken", userId, 30 * 24 * 60 * 60 * 1000L);

            // DB에 새로운 refreshToken으로 교체
            tokenService.updateRefreshToken(userId, refreshToken, newRefreshToken);

            AuthenticationResponse authenticationResponse = new AuthenticationResponse(newAccessToken, refreshToken);

            return BaseResponse.onSuccess(SuccessStatus._OK, authenticationResponse);

        }

        return BaseResponse.onFailure(ErrorStatus._UNAUTHORIZED, "Invalid or expired refresh token");
    }
}
