package com._oormthonUNIV.newnew.security.dto.cache;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBlackListCache {
    private UUID jti;
    private String encryptedToken;
    private LocalDateTime logoutTime;
    private LocalDateTime expiryDate;
    private Long userId;
}
