package com._oormthonUNIV.newnew.security.properties;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.security.Key;
import java.time.Duration;
import java.util.Objects;

@Getter
@Configuration
public class JwtProperties {

    private final Environment env;

    private final Key ACCESS_TOKEN_SECRET_KEY;
    private final Key REFRESH_TOKEN_SECRET_KEY;
    private final Duration REFRESH_TOKEN_TTL;
    private final Duration ACCESS_TOKEN_TTL;
    private final String TOKEN_ISSUER;

    public JwtProperties(Environment env) {
        this.env = env;
        this.ACCESS_TOKEN_SECRET_KEY = parseKey(env.getProperty("jwt.key.access-key"));
        this.REFRESH_TOKEN_SECRET_KEY = parseKey(env.getProperty("jwt.key.refresh-key"));
        long accessTtl =
                parseLongAndNullCheck(env.getProperty("jwt.ttl.access"));
        long refreshTtl =
                parseLongAndNullCheck(env.getProperty("jwt.ttl.refresh"));
        this.ACCESS_TOKEN_TTL = Duration.ofMinutes(accessTtl);
        this.REFRESH_TOKEN_TTL = Duration.ofDays(refreshTtl);
        this.TOKEN_ISSUER = env.getProperty("jwt.issuer");
    }

    private Long parseLongAndNullCheck(String value) {
        return Long.parseLong(Objects.requireNonNull(value));
    }

    private Key parseKey(String value) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(value));
    }
}

