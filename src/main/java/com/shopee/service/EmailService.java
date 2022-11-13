package com.shopee.service;

import com.shopee.request.email.SendEmailRequest;

import javax.mail.MessagingException;

public interface EmailService {
    void send(SendEmailRequest request, Boolean isHtmlFormat) throws MessagingException;
}
