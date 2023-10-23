package com.gustavohenning.dbecommercev1.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {

     void sendEmail(String toEmail, String body, String subject);

     void sendEmailWithAttachment(String emailTo, String body, String subject, String attachment) throws MessagingException;
}
