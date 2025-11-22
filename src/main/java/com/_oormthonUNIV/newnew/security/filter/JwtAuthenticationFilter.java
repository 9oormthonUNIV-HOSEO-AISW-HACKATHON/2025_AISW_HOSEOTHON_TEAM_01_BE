package com._oormthonUNIV.newnew.security.filter;


import com._oormthonUNIV.newnew.security.exception.AccessTokenBlackListErrorCode;
import com._oormthonUNIV.newnew.security.exception.AccessTokenBlackListException;
import com._oormthonUNIV.newnew.security.service.AccessTokenBlackListService;
import com._oormthonUNIV.newnew.security.service.impl.UserDetailServiceImpl;
import com._oormthonUNIV.newnew.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailServiceImpl userService;
    private final AccessTokenBlackListService blackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.resolveTokenFromHttpServletRequest(request);
        if (token != null) {
            // 토큰 검증
            jwtUtil.validateAccessToken(token);
            // Subject에서 UserId(UUID) 가져오기
            UUID userIdentificationId = jwtUtil.getSubjectFromAccessToken(token);
            UserDetails userDetails =
                    userService.loadUserByUsername(userIdentificationId.toString());

            //로그아웃리스트 확인
            UUID jwtJti = jwtUtil.getJtiFromAccessToken(token);
            if(blackListService.isExistByToken(token)){
                throw new AccessTokenBlackListException(
                        AccessTokenBlackListErrorCode.TOKEN_IS_BLACK_LIST
                );
            }

            // Spring Security 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities() // UserDetails 기반이면 GrantedAuthority 제공
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }
        filterChain.doFilter(request, response);
    }

}

