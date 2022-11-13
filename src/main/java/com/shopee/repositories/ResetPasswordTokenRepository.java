package com.shopee.repositories;

import com.shopee.entity.ResetPasswordTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordTokenEntity, Long> {
    ResetPasswordTokenEntity findByToken(String token);

    ResetPasswordTokenEntity findByResetPasswordId(Long resetPasswordId);

}
