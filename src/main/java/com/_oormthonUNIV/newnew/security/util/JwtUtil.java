package com._oormthonUNIV.newnew.security.util;

import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.security.exception.JwtUtilErrorCode;
import com._oormthonUNIV.newnew.security.exception.JwtUtilException;
import com._oormthonUNIV.newnew.security.properties.JwtProperties;
import com._oormthonUNIV.newnew.user.entity.Users;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private final JwtParser accessTokenParser;
    private final JwtParser refreshTokenParser;

    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.accessTokenParser = Jwts.parserBuilder()
                .requireIssuer(jwtProperties.getTOKEN_ISSUER())
                .setClock(() -> Date.from(TimeUtil.getNowSeoulInstant()))
                .setAllowedClockSkewSeconds(60)   // 시계 오차 허용 (필요시 조정)
                .setSigningKey(jwtProperties.getACCESS_TOKEN_SECRET_KEY())
                .build();
        this.refreshTokenParser = Jwts.parserBuilder()
                .requireIssuer(jwtProperties.getTOKEN_ISSUER())
                .setClock(() -> Date.from(TimeUtil.getNowSeoulInstant()))
                .setAllowedClockSkewSeconds(60)   // 시계 오차 허용 (필요시 조정)
                .setSigningKey(jwtProperties.getREFRESH_TOKEN_SECRET_KEY())
                .build();
    }

    // ====== 생성 ======
    public String createAccessToken(Users user) {
        return createToken(
                String.valueOf(user.getIdentificationId()),
                jwtProperties.getACCESS_TOKEN_SECRET_KEY(),
                jwtProperties.getACCESS_TOKEN_TTL()
        );
    }

    public String createRefreshToken(Users user) {
        return createToken(
                String.valueOf(user.getIdentificationId()),
                jwtProperties.getREFRESH_TOKEN_SECRET_KEY(),
                jwtProperties.getREFRESH_TOKEN_TTL()
        );
    }

    // ====== Claims ======
    public Claims getClaimsFromAccessToken(String token) {
        return parse(token, jwtProperties.getACCESS_TOKEN_SECRET_KEY());
    }

    public Claims getClaimsFromRefreshToken(String token) {
        return parse(token, jwtProperties.getREFRESH_TOKEN_SECRET_KEY());
    }

    // ====== subject ======
    public UUID getSubjectFromAccessToken(String token) {
        try {
            return UUID.fromString(getClaimsFromAccessToken(token).getSubject());
        } catch (IllegalArgumentException e) {
            log.warn("[JWT] 잘못된 형식의 토큰  {}", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_ILLEGAL_ARGUMENT);
        }
    }

    public UUID getSubjectFromRefreshToken(String token) {
        try {
            return UUID.fromString(getClaimsFromRefreshToken(token).getSubject());
        } catch (IllegalArgumentException e) {
            log.warn("[JWT] 잘못된 형식의 토큰  {}", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_ILLEGAL_ARGUMENT);
        }
    }

    // ====== jti ======
    public UUID getJtiFromAccessToken(String token) {
        try {
            return UUID.fromString(getClaimsFromAccessToken(token).getId());
        } catch (IllegalArgumentException e) {
            log.warn("[JWT] 잘못된 형식의 토큰  {}", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_ILLEGAL_ARGUMENT);
        }
    }

    public UUID getJtiFromRefreshToken(String token) {
        try {
            return UUID.fromString(getClaimsFromRefreshToken(token).getId());
        } catch (IllegalArgumentException e) {
            log.warn("[JWT] 잘못된 형식의 토큰  {}", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_ILLEGAL_ARGUMENT);
        }
    }

    // ====== remaining =====
    public String remainingRefreshToken(String refreshToken, Users user) {
        Date refreshTokenExpiration = getExpirationFromRefreshToken(refreshToken);
        LocalDateTime exp = TimeUtil.toLocalDateTime(refreshTokenExpiration);
        LocalDateTime now = TimeUtil.getNowSeoulLocalDateTime();

        Duration remaining = Duration.between(now, exp);
        if (remaining.isNegative() || remaining.isZero()) {
            // 이미 만료
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_EXPIRED);
        }
        // 남은 시간이 1일(24시간) 이하이면 새 토큰 발급
        if (remaining.compareTo(Duration.ofDays(1)) <= 0) {
            return createRefreshToken(user);
        }
        return null;
    }

    // ====== expiration ======
    public Date getExpirationFromAccessToken(String token) {
        return getClaimsFromAccessToken(token).getExpiration();
    }

    public Date getExpirationFromRefreshToken(String token) {
        return getClaimsFromRefreshToken(token).getExpiration();
    }

    // ====== 검증(간단: boolean) ======
    public void validateAccessToken(String token) {
        validateInternal(token, jwtProperties.getACCESS_TOKEN_SECRET_KEY());
    }

    public void validateRefreshToken(String token) {
        validateInternal(token, jwtProperties.getREFRESH_TOKEN_SECRET_KEY());
    }

    public String resolveTokenFromHttpServletRequest(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }


    private String createToken(String subject, Key key, Duration ttl) {
        Instant now = TimeUtil.getNowSeoulInstant();
        Instant exp = now.plus(ttl);

        return Jwts.builder()
                .setSubject(subject)                 // PK만 담음
                .setId(UUID.randomUUID().toString())               // jti
                .setIssuer(jwtProperties.getTOKEN_ISSUER())                  // iss
                .setIssuedAt(Date.from(now))        // iat
                .setExpiration(Date.from(exp))      // exp
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private JwtParser getParser(Key key) {
        if (key.equals(jwtProperties.getACCESS_TOKEN_SECRET_KEY())) {
            return accessTokenParser;
        } else if (key.equals(jwtProperties.getREFRESH_TOKEN_SECRET_KEY())) {
            return refreshTokenParser;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private Claims parse(String rawToken, Key key) {
        String token = stripBearerPrefix(rawToken);
        JwtParser parser = getParser(key);
        return parser.parseClaimsJws(token).getBody();
    }

    public String stripBearerPrefix(String token) {
        if (token == null) return null;
        String t = token.trim();
        if (t.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return t.substring(7).trim();
        }
        return t;
    }

    private void validateInternal(String rawToken, Key key) {
        String token = stripBearerPrefix(rawToken);
        try {
            JwtParser parser = getParser(key);
            parser.parseClaimsJws(token); // 성공하면 유효
        } catch (ExpiredJwtException e) {
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_MALFORMED);
        } catch (SecurityException e) {
            log.error("[JWT_EXCEPTION] SecurityException message -> {} ", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_BAD_SIGNATURE);
        } catch (IllegalArgumentException e) {
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_ILLEGAL_ARGUMENT);
        } catch (JwtException e) {
            log.error("[JWT_EXCEPTION] JwtException message -> {} ", e.getMessage());
            throw new JwtUtilException(JwtUtilErrorCode.TOKEN_OTHER);
        }
    }

}
