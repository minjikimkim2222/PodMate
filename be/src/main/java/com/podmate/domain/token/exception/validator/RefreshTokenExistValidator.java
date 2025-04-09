package com.podmate.domain.token.exception.validator;

import com.podmate.domain.token.domain.repository.TokenRepository;
import com.podmate.domain.token.exception.annotation.RefreshTokenExist;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenExistValidator implements ConstraintValidator<RefreshTokenExist, Long> {

    private final TokenRepository tokenRepository;
    @Override
    public void initialize(RefreshTokenExist constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    // 여기서 해당 리프레시 토큰 id가 우리 DB에 존재하는지 테스트!
    @Override
    public boolean isValid(Long tokenId, ConstraintValidatorContext context) {
        if (tokenId == null) {
            return false;
        }
        return tokenRepository.existsById(tokenId);
    }

}
