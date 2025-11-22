package com._oormthonUNIV.newnew.security.service.impl;


import com._oormthonUNIV.newnew.security.entity.RefreshToken;
import com._oormthonUNIV.newnew.security.exception.RefreshTokenErrorCode;
import com._oormthonUNIV.newnew.security.exception.RefreshTokenException;
import com._oormthonUNIV.newnew.security.repository.RefreshTokenRepository;
import com._oormthonUNIV.newnew.security.service.RefreshTokenService;
import com._oormthonUNIV.newnew.user.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Override
    public RefreshToken save(RefreshToken entity) {
        try{
            return repository.save(entity);
        }catch(Exception e){
            log.error("[REFRESH_TOKEN] [ERROR] save -> {}", e.getMessage());
            throw new RefreshTokenException(RefreshTokenErrorCode.REFRESH_TOKEN_SAVE_ERROR);
        }
    }

    @Override
    public RefreshToken getByJti(UUID id) {
        return repository.findById(id)
                .orElseThrow( () ->
                        new RefreshTokenException(RefreshTokenErrorCode.REFRESH_TOKEN_NOT_FOUND)
                );
    }

    @Override
    public List<RefreshToken> getByUser(Users user) {
        return List.of();
    }
}
