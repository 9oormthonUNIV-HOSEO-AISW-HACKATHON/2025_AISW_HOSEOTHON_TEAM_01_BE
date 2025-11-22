package com._oormthonUNIV.newnew.security.factory;


import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.security.dto.cache.AccessTokenBlackListCache;
import com._oormthonUNIV.newnew.security.dto.response.AllTokenResponse;
import com._oormthonUNIV.newnew.security.entity.RefreshToken;
import com._oormthonUNIV.newnew.security.util.JwtUtil;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.time.LocalDateTime;

public class SecurityFactory {

    public static RefreshToken toRefreshToken(
            JwtUtil jwtUtil, String rawRefreshToken
    ) {
        LocalDateTime expiryDate = TimeUtil.toLocalDateTime(
                jwtUtil.getExpirationFromRefreshToken(rawRefreshToken)
        );
        return RefreshToken.builder()
                .id(jwtUtil.getJtiFromRefreshToken(rawRefreshToken))
                .encryptedToken(rawRefreshToken)
                .expiryDate(expiryDate)
                .build();
    }

    public static AllTokenResponse toAllTokenResponse(
            JwtUtil jwtUtil, String rawRefreshToken,
            String rawAccessToken
    ) {
        LocalDateTime refreshTokenExpiryDate = TimeUtil.toLocalDateTime(
                jwtUtil.getExpirationFromRefreshToken(rawRefreshToken)
        );
        LocalDateTime accessTokenExpiryDate = TimeUtil.toLocalDateTime(
                jwtUtil.getExpirationFromAccessToken(rawAccessToken)
        );
        return AllTokenResponse.builder()
                .refreshToken(rawRefreshToken)
                .accessToken(rawAccessToken)
                .accessTokenExpiresAt(accessTokenExpiryDate)
                .refreshTokenExpiresAt(refreshTokenExpiryDate)
                .build();
    }

    public static AccessTokenBlackListCache toAccessTokenBlackListCache(
            String accessToken,
            Users user,
            JwtUtil jwtUtil
    ) {
        return AccessTokenBlackListCache.builder()
                .jti(jwtUtil.getJtiFromAccessToken(accessToken))
                .encryptedToken(accessToken)
                .expiryDate(TimeUtil.toLocalDateTime(jwtUtil.getExpirationFromAccessToken(accessToken)))
                .logoutTime(TimeUtil.getNowSeoulLocalDateTime())
                .userId(user.getId())
                .build();
    }

}

