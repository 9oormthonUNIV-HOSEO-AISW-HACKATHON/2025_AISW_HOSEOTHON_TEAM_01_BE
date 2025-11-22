package com._oormthonUNIV.newnew.user.repository;

import com._oormthonUNIV.newnew.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByIdentificationId(UUID identificationId);

    Optional<Users> findByLoginId(String loginId);

}
