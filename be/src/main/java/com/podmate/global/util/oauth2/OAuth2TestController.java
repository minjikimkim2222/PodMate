package com.podmate.global.util.oauth2;

import com.podmate.domain.user.domain.entity.User;
import com.podmate.domain.user.domain.repository.UserRepository;
import com.podmate.domain.user.exception.UserNotFoundException;
import com.podmate.global.util.jwt.JwtUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OAuth2TestController {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    @GetMapping("/api/auth/test-token")
    public ResponseEntity<?> getTestToken(@RequestParam Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());

        String accessToken = jwtUtil.createAccessToken("accessToken", user.getId(), user.getRole().getName(),
                30 * 60 * 1000L);
        String refreshToken = jwtUtil.createRefreshToken("refreshToken", user.getId(), 30 * 24 * 60 * 60 * 1000L);

        return ResponseEntity.ok(Map.of(
           "accessToken", accessToken,
           "refreshToken", refreshToken
        ));
    }

}
