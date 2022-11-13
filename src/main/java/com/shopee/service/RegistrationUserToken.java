package com.shopee.service;

import com.shopee.entity.RegistrationUserTokenEntity;
import com.shopee.entity.User;

public interface RegistrationUserToken {
    void createNewRegistrationUserToken(User user, final String token);

    RegistrationUserTokenEntity findByToken(String token);
}
