package com._oormthonUNIV.newnew.security.exception;

import com._oormthonUNIV.newnew.global.exception.GlobalBaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecurityFacadeException extends GlobalBaseException {
    private SecurityFacadeErrorCode errorCode;
}
