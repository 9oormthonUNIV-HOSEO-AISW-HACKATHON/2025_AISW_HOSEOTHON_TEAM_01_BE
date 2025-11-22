package com._oormthonUNIV.newnew.security.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum JwtUtilErrorCode implements GlobalErrorCode {

    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,6000, "만료된 토큰입니다."),
    TOKEN_BAD_SIGNATURE(HttpStatus.BAD_REQUEST,6001, "잘못된 토큰입니다."),
    TOKEN_MALFORMED(HttpStatus.BAD_REQUEST,6002, "잘못된 토큰입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST,6003, "잘못된 토큰입니다."),
    TOKEN_ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST,6004, "잘못된 형식의 토큰입니다."),
    TOKEN_OTHER(HttpStatus.INTERNAL_SERVER_ERROR,6005, "서버 오류입니다.");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

