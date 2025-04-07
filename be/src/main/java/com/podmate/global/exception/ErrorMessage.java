package com.podmate.global.exception;

// 비즈니스 로직 관련 에러 메세지 - application 계층과 관련이 있음
public enum ErrorMessage {
    // global
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다.", "g001"),

    // auth
    UNAUTHORIZED("권한이 없습니다", "a001"),

    // token
    REFRESH_TOKEN_NOT_FOUND("기존 리프레시 토큰을 찾을 수 없습니다.", "t001"),
    DOES_NOT_MATCH_REFRESH_TOKEN("기존 리프레시 토큰이 일치하지 않습니다.", "t002");

    private final String message;
    private final String code;

    ErrorMessage(String message, String code){
        this.message = message;
        this.code = code;
    }

    public String getCode(){
        return code;
    }

    public String getMessage() {
        return message;
    }
}
