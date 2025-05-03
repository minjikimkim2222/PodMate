package com.podmate.global.common.code.status;

import com.podmate.global.common.code.BaseErrorCode;
import com.podmate.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // --- Common ---
    _INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON400", "입력값이 올바르지 않습니다"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "권한이 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // --- Token ---
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_404", "리프레시 토큰을 찾을 수 없습니다."),
    TOKEN_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "TOKEN_401", "리프레시 토큰 인증 실패입니다."),

    // --- USER ---
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "유저를 찾을 수 없습니다."),

    // --- POD ---
    POD_NOT_FOUND(HttpStatus.NOT_FOUND, "POD_404", "팟을 찾을 수 없습니다."),

    // --- PODUSERMAPPING ---
    PODUSERMAPPING_NOT_FOUND(HttpStatus.NOT_FOUND, "PODUSERMAPPING_404", "팟ID에 맞는 유저를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
