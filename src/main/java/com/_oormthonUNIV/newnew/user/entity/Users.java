package com._oormthonUNIV.newnew.user.entity;

import com._oormthonUNIV.newnew.global.util.LongIdEntity;
import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import com._oormthonUNIV.newnew.user.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends LongIdEntity {

    @Column(columnDefinition = "binary(16)")
    private UUID identificationId;

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGeneration generation;

    @Column(nullable = false)
    private LocalDateTime birth;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Override
    public void onPrePersist() {
        this.identificationId = UUID.randomUUID();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = TimeUtil.getNowSeoulLocalDateTime();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public boolean isUpdated() {
        return this.updatedAt != null;
    }

    public void softDelete() {
        LocalDateTime now = TimeUtil.getNowSeoulLocalDateTime();
        this.updatedAt = now;
        this.deletedAt = now;
    }

}
