package com.podmate.global.util.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.podmate.domain.token.domain.entity.Token;
import com.podmate.domain.token.domain.repository.TokenRepository;
import com.podmate.domain.user.domain.entity.User;
import com.podmate.global.util.jwt.JwtUtil;
import com.podmate.global.util.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;




// 카카오 로그인 성공 시, 콜백 핸들러
// 1. JWT 토큰 발급
// - 이때, JWT payload는 보안상 최소한의 정보(userId, role)만 담겠다
// 2. refreshToken만 DB에 저장
// 3. JSON 응답으로, accessToken과 refreshToken 을 반환해준다.
@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 1. CustomOAuth2UserService에서 설정한 OAuth2User 정보 가져오기
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        User user = customUserDetails.getUser();
        Long userId = customUserDetails.getUserId();
        String email = customUserDetails.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        log.info("user, userId, email, role :: {} {} {} {}", user, userId, email, role);

        // 2. 1)의 사용자 정보를 담아, accessToken과 refreshToken 발행
        String accessToken = jwtUtil.createAccessToken("accessToken", userId, role, 30 * 60 * 1000L);  // 유효기간 30분
        String refreshToken = jwtUtil.createRefreshToken("refreshToken", userId, 30 * 24 * 60 * 60 * 1000L);    // 유효기간 30일

        // 3. refreshToken을 DB에 저장 -- user1명당, token 1개로 제한해놓아서 업데이트 로직으로 변경
        Optional<Token> existingToken = tokenRepository.findByUserId(userId);

        if (existingToken.isPresent()){
            log.info("이미 토큰이 존재합니다. 새로운 토큰으로 업데이트합니다!");
            Token token = existingToken.get();
            token.update(refreshToken, LocalDateTime.now().plusDays(30));

            tokenRepository.save(token);

        } else { // 기존 토큰 존재하지 않음
            Token refreshTokenEntity = Token.toEntity(user, refreshToken, LocalDateTime.now().plusDays(30));
            tokenRepository.save(refreshTokenEntity);
        }


        // 4. JSON 응답으로, accessToken과 refreshToken 을 반환해준다.
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ObjectMapper objectMapper = new ObjectMapper(); // 객체 -> json 문자열로 변환
        String body = objectMapper.writeValueAsString(
                Map.of(
                        "accessToken", accessToken,
                        "refreshToken", refreshToken
                )
        );
        // response.getWriter().write(body);
			String redirectUrl = UriComponentsBuilder
        .fromUriString("http://localhost:5173/oauth/redirect") // ✅ 프론트 리디렉션 주소
        .queryParam("status", "success")
        .queryParam("accessToken", accessToken)
        .queryParam("refreshToken", refreshToken)
        .build()
        .toUriString();

response.sendRedirect(redirectUrl);

    }


}
