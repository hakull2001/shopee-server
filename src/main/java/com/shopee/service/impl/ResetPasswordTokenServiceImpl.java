package com.shopee.service.impl;

import com.shopee.entity.ResetPasswordTokenEntity;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.repositories.ResetPasswordTokenRepository;
import com.shopee.service.ResetPasswordTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Override
    public void createTokenToResetPassword(ResetPasswordTokenEntity resetPasswordToken) {
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public ResetPasswordTokenEntity findByToken(String token) {
        ResetPasswordTokenEntity resetPasswordTokenEntity = resetPasswordTokenRepository.findByToken(token);
        if(resetPasswordTokenEntity == null)
            throw new NotFoundException("Cannot reset your password");
        return resetPasswordTokenEntity;
    }

    @Override
    public void deleteByResetPasswordId(Long resetPasswordId) {
        ResetPasswordTokenEntity resetPasswordTokenEntity = resetPasswordTokenRepository.findByResetPasswordId(resetPasswordId);
        if(resetPasswordTokenEntity == null)
            throw new AppException("Cannot reset your password");

        resetPasswordTokenRepository.delete(resetPasswordTokenEntity);
    }
}
