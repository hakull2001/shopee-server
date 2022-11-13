package com.shopee.service.impl;

import com.shopee.request.email.SendEmailRequest;
import com.shopee.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void send(SendEmailRequest request, Boolean isHtmlFormat) throws MessagingException {
        if (isHtmlFormat == null)
            isHtmlFormat = false;

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setSubject(request.getTitle());
        helper.setText(request.getContent(), isHtmlFormat);
        helper.setTo(request.getTo());

        mailSender.send(mimeMessage);
    }
}
