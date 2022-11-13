package com.shopee.service.impl;

import com.shopee.entity.RegistrationUserTokenEntity;
import com.shopee.entity.User;
import com.shopee.enumerations.UserStatus;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.ErrorResponse;
import com.shopee.exceptions.NotFoundException;
import com.shopee.repositories.RegistrationUserTokenRepository;
import com.shopee.repositories.UserRepository;
import com.shopee.request.user.SignUpRequest;
import com.shopee.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegistrationUserTokenRepository registrationUserTokenRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User not found");
        return user;
    }

    @Override
    public User signUp(SignUpRequest request) {
        if(userRepository.findByUsername(request.getUsername()) != null)
            throw new AppException("Username has exists");
        if(userRepository.findByEmail(request.getEmail()) != null)
            throw new AppException("Email has exists");

        User newUser = modelMapper.map(request, User.class);
        return userRepository.save(newUser);

    }

    @Override
    public void activeUser(String token) {
        RegistrationUserTokenEntity registrationUserToken = registrationUserTokenRepository.findByToken(token);
        if(registrationUserToken == null)
            throw new AppException("Can not active this user");

        User user = registrationUserToken.getUser();

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        registrationUserTokenRepository.deleteByRegistrationId(registrationUserToken.getRegistrationId());
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if(user == null)
            throw new NotFoundException("This user is not exists");
        return user;
    }

    @Override
    public User findByUserId(Long userId) {
        User user = userRepository.findByUserId(userId);
        if(user == null)
            throw new NotFoundException("Can not find this user");
        return user;
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

}
