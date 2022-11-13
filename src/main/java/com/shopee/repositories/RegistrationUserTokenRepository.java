package com.shopee.repositories;

import com.shopee.entity.RegistrationUserTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface RegistrationUserTokenRepository extends JpaRepository<RegistrationUserTokenEntity, Long> {
    RegistrationUserTokenEntity findByToken(String token);

    @Transactional
    void deleteByRegistrationId(Long registrationId);
}
