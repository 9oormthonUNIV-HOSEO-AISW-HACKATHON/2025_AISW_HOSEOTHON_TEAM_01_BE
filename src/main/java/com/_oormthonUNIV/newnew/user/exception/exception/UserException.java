package com._oormthonUNIV.newnew.user.exception.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalBaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserException extends GlobalBaseException {
    private UserErrorCode errorCode;
}
