package com._oormthonUNIV.newnew.security.service.impl;


import com._oormthonUNIV.newnew.security.dto.cache.AccessTokenBlackListCache;
import com._oormthonUNIV.newnew.security.factory.SecurityFactory;
import com._oormthonUNIV.newnew.security.repository.cache.AccessTokenBlackListCacheStore;
import com._oormthonUNIV.newnew.security.service.AccessTokenBlackListService;
import com._oormthonUNIV.newnew.security.util.JwtUtil;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessTokenBlackListServiceImpl implements AccessTokenBlackListService {

    private final AccessTokenBlackListCacheStore cache;
    private final JwtUtil jwtUtil;

    @Override
    public AccessTokenBlackListCache getBlackList(String accessToken) {
        UUID jti = jwtUtil.getJtiFromAccessToken(accessToken);
        return cache.getByJTI(jti);
    }

    @Override
    public Optional<AccessTokenBlackListCache> findBlackList(String accessToken) {
        UUID jti = jwtUtil.getJtiFromAccessToken(accessToken);
        AccessTokenBlackListCache blackList = cache.getByJTI(jti);
        return Optional.of(blackList);
    }

    @Override
    public void saveBlackList(String accessToken, Users user) {
        cache.setAccessTokenBlackListCache(
                SecurityFactory.toAccessTokenBlackListCache(accessToken, user,jwtUtil)
        );
    }

    @Override
    public boolean isExistByToken(String accessToken) {
        return cache.isExist(jwtUtil.getJtiFromAccessToken(accessToken));
    }
}
