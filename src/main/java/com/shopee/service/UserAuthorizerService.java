package com.shopee.service;

import org.springframework.security.core.Authentication;

public interface UserAuthorizerService {
    boolean isAdmin(Authentication authentication);

    boolean isMember(Authentication authentication);
    boolean isYourself(Authentication authentication, Long userId);
}
