package com.podmate.global.exception;

public class TokenUnauthorizedException extends UnauthorizedException{
    public TokenUnauthorizedException(){
        super(ErrorMessage.TOKEN_UNAUTHORIZED);
    }
}
