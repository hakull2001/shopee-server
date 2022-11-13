package com.shopee.service;

import com.shopee.entity.ResetPasswordTokenEntity;

public interface ResetPasswordTokenService {
    void createTokenToResetPassword(ResetPasswordTokenEntity resetPasswordToken);

    ResetPasswordTokenEntity findByToken(String token);

    void deleteByResetPasswordId(Long resetPasswordId);
}
