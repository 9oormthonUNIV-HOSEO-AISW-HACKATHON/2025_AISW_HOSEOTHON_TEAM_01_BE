package com._oormthonUNIV.newnew.security.repository.cache.impl;


import com._oormthonUNIV.newnew.global.service.LocalCacheService;
import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.security.dto.cache.AccessTokenBlackListCache;
import com._oormthonUNIV.newnew.security.repository.cache.AccessTokenBlackListCacheStore;
import com._oormthonUNIV.newnew.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccessTokenBlackListCacheStoreImpl implements AccessTokenBlackListCacheStore {

    private final LocalCacheService<String, AccessTokenBlackListCache> localCacheService;
    private final JwtUtil jwtUtil;
    private final String CACHE_ID = "BL";


    @Override
    public void setAccessTokenBlackListCache(AccessTokenBlackListCache cache) {
        Date date = jwtUtil.getExpirationFromAccessToken(cache.getEncryptedToken());
        long expire = TimeUtil.toEpochMilli(TimeUtil.toLocalDateTime(date));
        long now = TimeUtil.getNowSeoulInstant().toEpochMilli();
        long diff = expire - now;
        long ttl = diff > 0 ? diff : 0L;
        localCacheService.put(CACHE_ID + cache.getJti(), cache, ttl);
    }

    @Override
    public boolean isExist(UUID jti) {
        return localCacheService.get(CACHE_ID + jti) != null;
    }

    @Override
    public AccessTokenBlackListCache getByJTI(UUID jti) {
        return localCacheService.get(CACHE_ID + jti);
    }

    @Override
    public void removeBlackList(UUID jti) {
        localCacheService.remove(CACHE_ID + jti);
    }
}
