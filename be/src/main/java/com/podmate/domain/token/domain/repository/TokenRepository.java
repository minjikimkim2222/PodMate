package com.podmate.domain.token.domain.repository;

import com.podmate.domain.token.domain.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserId(Long userId);
}

