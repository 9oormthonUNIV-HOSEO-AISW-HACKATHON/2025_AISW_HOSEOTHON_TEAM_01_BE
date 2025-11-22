package com._oormthonUNIV.newnew.security.exception;


import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum RefreshTokenErrorCode implements GlobalErrorCode {

    REFRESH_TOKEN_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 4001, "로그인 정보 저장 실패"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, 4004, "로그인 정보 검색 실패");

    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
