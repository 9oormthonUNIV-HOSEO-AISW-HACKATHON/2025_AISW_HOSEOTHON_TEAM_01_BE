package com._oormthonUNIV.newnew.user.service;

import com._oormthonUNIV.newnew.user.entity.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<Users> findByIdentificationId(UUID identificationId);

    Optional<Users> findByLoginIdAndPassword(String loginId);

    Users getByIdentificationId(UUID identificationId);

    Users getByLoginId(String loginId);

    Users save(Users user);
}
