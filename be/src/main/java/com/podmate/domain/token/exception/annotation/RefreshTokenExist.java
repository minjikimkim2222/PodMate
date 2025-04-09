package com.podmate.domain.token.exception.annotation;

import com.podmate.domain.token.exception.validator.RefreshTokenExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RefreshTokenExistValidator.class)
@Documented
public @interface RefreshTokenExist {
    String message() default "해당 리프레시토큰의 ID가 존재하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
