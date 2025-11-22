package com._oormthonUNIV.newnew.security.entity;

import com._oormthonUNIV.newnew.global.entity.TimeRecordEntity;
import com._oormthonUNIV.newnew.user.entity.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends TimeRecordEntity {

    @Id
    private UUID id;// jti

    private String encryptedToken;

    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
