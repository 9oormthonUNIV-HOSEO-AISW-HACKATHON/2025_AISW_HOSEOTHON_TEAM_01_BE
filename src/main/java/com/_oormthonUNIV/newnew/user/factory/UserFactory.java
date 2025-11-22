package com._oormthonUNIV.newnew.user.factory;

import com._oormthonUNIV.newnew.global.util.TimeUtil;
import com._oormthonUNIV.newnew.security.dto.reqeust.SignUpRequest;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.entity.enums.UserGeneration;
import com._oormthonUNIV.newnew.user.entity.enums.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public class UserFactory {
    public static Users toUsers(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return Users.builder()
                .loginId(request.getLoginId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .identificationId(UUID.randomUUID())
                .birth(request.getBirth().atStartOfDay())
                .nickname(request.getNickname())
                .generation(UserGeneration.fromBirthYear(request.getBirth().getYear()))
                .build();
    }
}