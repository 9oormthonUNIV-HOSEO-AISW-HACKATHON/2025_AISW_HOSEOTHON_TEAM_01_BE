package com._oormthonUNIV.newnew.security.repository.cache;

import com._oormthonUNIV.newnew.security.dto.cache.AccessTokenBlackListCache;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.UUID;

public interface AccessTokenBlackListCacheStore {
    void setAccessTokenBlackListCache(AccessTokenBlackListCache cache);

    boolean isExist(UUID jti);

    AccessTokenBlackListCache getByJTI(UUID jti);

    void removeBlackList(UUID jti);
}

