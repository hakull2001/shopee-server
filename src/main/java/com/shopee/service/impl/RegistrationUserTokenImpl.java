package com.shopee.service.impl;

import com.shopee.entity.RegistrationUserTokenEntity;
import com.shopee.entity.User;
import com.shopee.exceptions.NotFoundException;
import com.shopee.repositories.RegistrationUserTokenRepository;
import com.shopee.service.RegistrationUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationUserTokenImpl implements RegistrationUserToken {
    @Autowired
    private RegistrationUserTokenRepository registrationUserTokenRepository;

    @Override
    public void createNewRegistrationUserToken(User user, String token) {
        RegistrationUserTokenEntity tokenEntity = new RegistrationUserTokenEntity(user, token);
        registrationUserTokenRepository.save(tokenEntity);
    }

    @Override
    public RegistrationUserTokenEntity findByToken(String token) {
        RegistrationUserTokenEntity registrationUserTokenEntity = registrationUserTokenRepository.findByToken(token);
        if(registrationUserTokenEntity == null)
            throw new NotFoundException("Cannot active this account");
        return registrationUserTokenEntity;
    }
}
