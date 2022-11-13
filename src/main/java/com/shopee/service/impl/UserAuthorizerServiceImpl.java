package com.shopee.service.impl;

import com.shopee.entity.User;
import com.shopee.service.UserAuthorizerService;
import com.shopee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service("userAuthorizer")
public class UserAuthorizerServiceImpl implements UserAuthorizerService {
    @Autowired
    private UserService userService;

    @Override
    public boolean isAdmin(Authentication authentication) {
        return Arrays.toString(authentication.getAuthorities().toArray()).contains("Admin");
    }

    @Override
    public boolean isMember(Authentication authentication) {
        return Arrays.toString(authentication.getAuthorities().toArray()).contains("Member");
    }

    @Override
    public boolean isYourself(Authentication authentication, Long userId) {
        org.springframework.security.core.userdetails.User userAuth = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        User user = userService.findByUsername(userAuth.getUsername());
        if (!Objects.equals(user.getUserId(), userId)) {
            throw new AccessDeniedException("Token has does not exit.");
        }
        return true;
    }
}
