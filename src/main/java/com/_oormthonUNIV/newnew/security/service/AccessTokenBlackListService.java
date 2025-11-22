package com._oormthonUNIV.newnew.security.service;

import com._oormthonUNIV.newnew.security.dto.cache.AccessTokenBlackListCache;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.Optional;

public interface AccessTokenBlackListService {
    AccessTokenBlackListCache getBlackList(String accessToken);

    Optional<AccessTokenBlackListCache> findBlackList(String accessToken);

    void saveBlackList(String accessToken, Users user);

    boolean isExistByToken(String accessToken);
}
