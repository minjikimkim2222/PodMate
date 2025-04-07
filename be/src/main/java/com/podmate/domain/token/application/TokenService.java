package com.podmate.domain.token.application;

public interface TokenService {
    public Boolean validateToken(String token, Long userId);
    public void updateRefreshToken(Long userId, String oldRefreshToken, String newRefreshToken);
}
