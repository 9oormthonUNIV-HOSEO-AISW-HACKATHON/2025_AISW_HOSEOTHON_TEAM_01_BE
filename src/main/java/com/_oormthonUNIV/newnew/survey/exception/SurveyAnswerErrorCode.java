package com._oormthonUNIV.newnew.survey.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum SurveyAnswerErrorCode  implements GlobalErrorCode {

    SURVEY_ANSWER_DUPLICATED(HttpStatus.BAD_REQUEST,2002,"같은 뉴스에 다시 설문을 할 수 없습니다."),
    SURVEY_BAD_ACCESS(HttpStatus.FORBIDDEN, 2003, "설문 통계 접근 권한이 없습니다."),
    AI_REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, 2004,"설문 통계가 존재하지 않습니다.");

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
