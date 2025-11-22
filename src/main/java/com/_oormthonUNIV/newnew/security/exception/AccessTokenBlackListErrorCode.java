package com._oormthonUNIV.newnew.security.exception;


import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AccessTokenBlackListErrorCode implements GlobalErrorCode {

    TOKEN_IS_BLACK_LIST(HttpStatus.BAD_REQUEST, 5001, "로그아웃된 유저입니다.");

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
