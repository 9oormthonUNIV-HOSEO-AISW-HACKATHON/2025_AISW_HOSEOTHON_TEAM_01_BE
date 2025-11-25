package com._oormthonUNIV.newnew.news.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalBaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsException extends GlobalBaseException {
    private final NewsErrorCode errorCode;
}
