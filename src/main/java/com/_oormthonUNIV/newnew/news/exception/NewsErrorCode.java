package com._oormthonUNIV.newnew.news.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum NewsErrorCode implements GlobalErrorCode {

    NEWS_NOT_FOUND(HttpStatus.NOT_FOUND, 2001, "존재하지 않는 뉴스입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 2002, "잘못된 뉴스 요청입니다.");

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
