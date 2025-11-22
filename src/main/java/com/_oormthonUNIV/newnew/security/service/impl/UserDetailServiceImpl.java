package com._oormthonUNIV.newnew.security.service.impl;


import com._oormthonUNIV.newnew.security.entity.UserDetailImpl;
import com._oormthonUNIV.newnew.user.entity.Users;
import com._oormthonUNIV.newnew.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    public UserDetailServiceImpl(UserRepository r) {
        this.repository = r;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UUID identificationId = UUID.fromString(username);
        Users user = repository.findByIdentificationId(identificationId)
                .orElseThrow( () -> new UsernameNotFoundException("user not found with identificationId : " + username));
        return new UserDetailImpl(user);
    }
}
