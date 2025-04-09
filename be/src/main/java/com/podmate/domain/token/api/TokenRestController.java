package com.podmate.domain.token.api;

import com.podmate.domain.token.application.TokenService;
import com.podmate.domain.token.dto.TokenRequestDto;
import com.podmate.domain.token.dto.TokenResponseDto;
import com.podmate.domain.token.dto.TokenResponseDto.TokenInfo;
import com.podmate.domain.token.exception.annotation.RefreshTokenExist;
import com.podmate.global.common.code.status.ErrorStatus;
import com.podmate.global.common.code.status.SuccessStatus;
import com.podmate.global.common.response.BaseResponse;
import com.podmate.global.util.jwt.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.podmate.domain.token.dto.TokenResponseDto.AuthenticationResponse;

// Controller에는 오직 Service 로직 위임만을 해야 하는데, 비즈니스 로직이 보인다. 리팩토링 함..
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refresh-token")
@Validated
public class TokenRestController {
    private final TokenService tokenService;
    @PostMapping
    public BaseResponse<TokenResponseDto.AuthenticationResponse> refreshToken(
            @RequestBody @Valid TokenRequestDto.RefreshTokenRequest request){

        TokenResponseDto.AuthenticationResponse response = tokenService.reissueToken(request.getRefreshToken());

        return BaseResponse.onSuccess(SuccessStatus.TOKEN_REISSUE_SUCCESS, response);

    }

    // 테스트용 -- annotation, validator 예제용 컨트롤러
    // 검증은 validator에서, 데이터 활용은 service에서
    @GetMapping("/{refreshtoken-id}")
    public BaseResponse<TokenResponseDto.TokenInfo> getRefreshToken(
            @PathVariable(name = "refreshtoken-id") @Valid @RefreshTokenExist Long refreshTokenId){
        TokenResponseDto.TokenInfo response = tokenService.getTokenInfo(refreshTokenId);

        return BaseResponse.onSuccess(SuccessStatus._OK, response);
    }
}
