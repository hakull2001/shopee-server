package com.shopee.controller;

import com.shopee.entity.ResetPasswordTokenEntity;
import com.shopee.entity.User;
import com.shopee.exceptions.AppException;
import com.shopee.exceptions.NotFoundException;
import com.shopee.request.email.SendEmailRequest;
import com.shopee.request.user.ForgotPasswordRequest;
import com.shopee.request.user.Login;
import com.shopee.request.user.ResetPasswordRequest;
import com.shopee.request.user.SignUpRequest;
import com.shopee.response.UserInformationResponse;
import com.shopee.service.EmailService;
import com.shopee.service.RegistrationUserToken;
import com.shopee.service.ResetPasswordTokenService;
import com.shopee.service.UserService;
import com.shopee.service.impl.MyUserDetailsServiceImpl;
import com.shopee.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final String MSG_SUBJECT_MAIL = "msg.subject.mail";
    private static final String MSG_CONTENT_MAIL = "msg.content.mail";
    private static final String MSC_SIGN_UP_SUCCESS = "msg.signup.success";
    private static final String MSG_LOGIN_FAIL = "msg.login.fail";
    private static final String MSG_NOT_FOUND_USER = "msg.not.found.user";
    private static final String MSG_SUBJECT_FORGOT_PASSWORD = "msg.subject.forgot.password";

    private static final String MSG_CONTENT_FORGOT_PASSWORD = "msg.content.forgot.password";

    private static final String MSG_FORGOT_PASSWORD_SUCCESS = "msg.forgot.password.success";

    private static final String MSG_RESET_PASSWORD_SUCCESS = "msg.reset.password.success";

    private static final String MSG_ACTIVE_USER_SUCCESS = "msg.active.user.success";
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsServiceImpl myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationUserToken registrationUserToken;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody @Valid SignUpRequest request) throws MessagingException {
        User user = userService.signUp(request);
        final String token = jwtUtil.generateToken(myUserDetailsService.loadUserByUsername(user.getUsername()));
        registrationUserToken.createNewRegistrationUserToken(user,token);
        emailService.send(new SendEmailRequest(
                request.getEmail(),
                MSG_CONTENT_MAIL + token,
                MSG_SUBJECT_MAIL
        ), true);
        return new ResponseEntity<>(MSC_SIGN_UP_SUCCESS, HttpStatus.CREATED);
    }

    @PostMapping("/active/{token}")
    public ResponseEntity<?> activeUser(@PathVariable(value = "token") String token){
        userService.activeUser(token);
        return new ResponseEntity<>(MSG_ACTIVE_USER_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Login login){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    login.getUsername(), login.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AppException(MSG_LOGIN_FAIL);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(login.getUsername());
        final String token = jwtUtil.generateToken(userDetails);
        User user = userService.findByUsername(login.getUsername());
        user.setLastLogin(Instant.now());
        return new ResponseEntity<>(new UserInformationResponse(token, user), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) throws MessagingException {
        User user = userService.findByEmail(request.getEmail());
        if(user == null)
            throw new NotFoundException(MSG_NOT_FOUND_USER);

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());
        final String token = jwtUtil.generateToken(userDetails);

        resetPasswordTokenService.createTokenToResetPassword(
                new ResetPasswordTokenEntity(user, token)
        );

        emailService.send(new SendEmailRequest(request.getEmail(),
                MSG_CONTENT_FORGOT_PASSWORD + token,
                MSG_SUBJECT_FORGOT_PASSWORD),
                true);

        return new ResponseEntity<>(MSG_FORGOT_PASSWORD_SUCCESS, HttpStatus.OK);
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable(value = "token") String token, @RequestBody @Valid ResetPasswordRequest request){
        ResetPasswordTokenEntity resetPasswordTokenEntity = resetPasswordTokenService.findByToken(token);
        if(!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new AppException("Password must match");

        User user = userService.findByUserId(resetPasswordTokenEntity.getUser().getUserId());
        user.setPassword(request.getNewPassword());
        userService.updateUser(user);

        resetPasswordTokenService.deleteByResetPasswordId(resetPasswordTokenEntity.getResetPasswordId());
        return new ResponseEntity<>(MSG_RESET_PASSWORD_SUCCESS, HttpStatus.OK);
    }
}
