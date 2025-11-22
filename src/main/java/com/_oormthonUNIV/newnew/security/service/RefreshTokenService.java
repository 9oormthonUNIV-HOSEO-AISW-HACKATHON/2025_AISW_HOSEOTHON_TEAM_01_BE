package com._oormthonUNIV.newnew.security.service;

import com._oormthonUNIV.newnew.security.entity.RefreshToken;
import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.List;
import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken save(RefreshToken entity);

    RefreshToken getByJti(UUID id);

    List<RefreshToken> getByUser(Users user);
}
