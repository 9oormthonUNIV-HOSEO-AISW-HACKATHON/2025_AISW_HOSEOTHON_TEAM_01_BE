package com._oormthonUNIV.newnew.security.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "AccessToken 재발급 요철 DTO")
public class AccessTokenReIssueRequest {

    @Schema(description = "리프레시 토큰 (Bearer 삭제)")
    public String refreshToken;
}
