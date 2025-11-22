package com._oormthonUNIV.newnew.security.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum SecurityFacadeErrorCode implements GlobalErrorCode {
    LOGIN_FAILED_ERROR(HttpStatus.BAD_REQUEST, 2001, "회원을 찾을 수 없습니다.");

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
        return message;
    }
}

