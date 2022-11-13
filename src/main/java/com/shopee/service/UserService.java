package com.shopee.service;

import com.shopee.entity.User;
import com.shopee.request.user.SignUpRequest;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User findByUsername(String username);

    User signUp(SignUpRequest request);

    void activeUser(String token);

    User findByEmail(String email);

    User findByUserId(Long userId);

    void updateUser(User user);

}
