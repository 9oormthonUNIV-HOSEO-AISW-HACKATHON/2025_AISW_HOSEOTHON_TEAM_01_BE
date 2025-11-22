package com._oormthonUNIV.newnew.security.facade;

import com._oormthonUNIV.newnew.security.dto.reqeust.AccessTokenReIssueRequest;
import com._oormthonUNIV.newnew.security.dto.reqeust.LoginRequest;
import com._oormthonUNIV.newnew.security.dto.reqeust.SignUpRequest;
import com._oormthonUNIV.newnew.security.dto.response.AllTokenResponse;
import com._oormthonUNIV.newnew.security.entity.RefreshToken;
import com._oormthonUNIV.newnew.security.exception.SecurityFacadeErrorCode;
import com._oormthonUNIV.newnew.security.exception.SecurityFacadeException;
import com._oormthonUNIV.newnew.security.factory.SecurityFactory;
import com._oormthonUNIV.newnew.security.service.AccessTokenBlackListService;
import com._oormthonUNIV.newnew.security.service.RefreshTokenService;
import com._oormthonUNIV.newnew.security.util.JwtUtil;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.factory.UserFactory;
import com._oormthonUNIV.newnew.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityFacade {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenBlackListService accessTokenBlackListService;
    private final PasswordEncoder passwordEncoder;

    public AllTokenResponse signUpRequest(SignUpRequest request) {
        Users userBeforeSave =
                UserFactory.toUsers(request, passwordEncoder);
        Users userAfterSave = userService.save(userBeforeSave);

        String accessToken = jwtUtil.createAccessToken(userAfterSave);
        String refreshToken = jwtUtil.createRefreshToken(userAfterSave);
        RefreshToken refreshTokenEntity =
                SecurityFactory.toRefreshToken(jwtUtil, refreshToken);
        refreshTokenService.save(refreshTokenEntity);
        return SecurityFactory.toAllTokenResponse(jwtUtil, refreshToken, accessToken);
    }

    public AllTokenResponse loginRequest(LoginRequest request) {
        Users user = userService.getByLoginId(request.getLoginId());

        if (user.getLoginId().equals(request.getLoginId())
                && passwordEncoder.matches(request.getPassword(), user.getPassword())
        ) {
            String accessToken = jwtUtil.createAccessToken(user);
            String refreshToken = jwtUtil.createRefreshToken(user);

            refreshTokenService.save(
                    SecurityFactory.toRefreshToken(jwtUtil, refreshToken)
            );

            return SecurityFactory.toAllTokenResponse(jwtUtil, refreshToken, accessToken);
        } else {
            throw new SecurityFacadeException(SecurityFacadeErrorCode.LOGIN_FAILED_ERROR);
        }

    }

    public void logout(String authHeader, Users user) {
        String accessToken = jwtUtil.stripBearerPrefix(authHeader);
        accessTokenBlackListService.saveBlackList(accessToken, user);
    }

    public AllTokenResponse accessTokenReIssue(AccessTokenReIssueRequest request) {
        String rawRefreshToken = request.getRefreshToken();
        jwtUtil.validateRefreshToken(rawRefreshToken);

        UUID userIdentificationId = jwtUtil.getSubjectFromAccessToken(rawRefreshToken);
        Users user = userService.getByIdentificationId(userIdentificationId);

        String accessToken = jwtUtil.createAccessToken(user);
        //refreshToken 재발급 조건에 안맞을시 null
        String refreshToken = jwtUtil.remainingRefreshToken(rawRefreshToken, user);
        if (refreshToken == null || refreshToken.isEmpty()) {
            return SecurityFactory.toAllTokenResponse(jwtUtil, refreshToken, accessToken);
        }

        RefreshToken refreshTokenEntity =
                SecurityFactory.toRefreshToken(jwtUtil, refreshToken);
        refreshTokenService.save(refreshTokenEntity);

        return SecurityFactory.toAllTokenResponse(jwtUtil, refreshToken, accessToken);

    }

}
