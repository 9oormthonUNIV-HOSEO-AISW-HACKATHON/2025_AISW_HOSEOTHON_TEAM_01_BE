package com._oormthonUNIV.newnew.user.service.impl;

import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.exception.exception.UserErrorCode;
import com._oormthonUNIV.newnew.user.exception.exception.UserException;
import com._oormthonUNIV.newnew.user.repository.UserRepository;
import com._oormthonUNIV.newnew.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public Optional<Users> findByIdentificationId(UUID identificationId) {
        return repository.findByIdentificationId(identificationId);
    }

    @Override
    public Optional<Users> findByLoginIdAndPassword(String loginId) {
        return repository.findByLoginId(loginId);
    }

    @Override
    public Users getByIdentificationId(UUID identificationId) {
        return repository.findByIdentificationId(identificationId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND_ERROR));
    }

    @Override
    public Users getByLoginId(String loginId) {
        return repository.findByLoginId(loginId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND_ERROR));
    }

    @Override
    public Users save(Users user) {
        try {
            return repository.save(user);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
