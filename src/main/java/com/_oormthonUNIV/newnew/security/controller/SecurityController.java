package com._oormthonUNIV.newnew.security.controller;

import com._oormthonUNIV.newnew.security.dto.reqeust.AccessTokenReIssueRequest;
import com._oormthonUNIV.newnew.security.dto.reqeust.LoginRequest;
import com._oormthonUNIV.newnew.security.dto.reqeust.SignUpRequest;
import com._oormthonUNIV.newnew.security.dto.response.AllTokenResponse;
import com._oormthonUNIV.newnew.security.entity.UserDetailImpl;
import com._oormthonUNIV.newnew.security.facade.SecurityFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "auth", description = "인증관련 API")
public class SecurityController {

    private final SecurityFacade facade;

    @PostMapping("/auth/login")
    @ApiResponse(responseCode = "200", description = "성공: 토큰 반환")
    @Operation(summary = "로그인", description = "이메일/비밀번호로 로그인 후 액세스/리프레시 토큰 반환")
    public ResponseEntity<AllTokenResponse> login(@RequestBody LoginRequest loginRequest) {
        AllTokenResponse response = facade.loginRequest(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/logout")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    @Operation(summary = "로그아웃", description = "Authorization 헤더의 액세스 토큰을 블랙리스트에 추가하고 리프레시 토큰도 무효화")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization") String authHeader,
            @AuthenticationPrincipal UserDetailImpl userDetail
    ) {
        facade.logout(authHeader, userDetail.getUser());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/sign-up")
    @Operation(summary = "회원가입", description = "회원가입 후 토큰 반환")
    public ResponseEntity<AllTokenResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(facade.signUpRequest(signUpRequest));
    }

    @PostMapping("/auth/access/reissue")
    @Operation(summary = "액세스토큰 재발급", description = "리프레시 토큰으로 액세스 토큰 재발급")
    public ResponseEntity<AllTokenResponse> reissue(@RequestBody AccessTokenReIssueRequest request) {
        return ResponseEntity.ok(facade.accessTokenReIssue(request));
    }
}
