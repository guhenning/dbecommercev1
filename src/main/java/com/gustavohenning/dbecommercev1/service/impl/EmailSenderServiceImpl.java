package com.gustavohenning.dbecommercev1.service.impl;

import com.gustavohenning.dbecommercev1.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String emailTo, String body, String subject) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("hntechdevelopment@gmail.com");
        message.setTo(emailTo);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
    }


    public void sendEmailWithAttachment(String emailTo, String body, String subject, String attachment) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom("hntechdevelopment@gmail.com");
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);
        FileSystemResource fileSystem = new FileSystemResource(new File(attachment));
        mimeMessageHelper.addAttachment(fileSystem.getFilename(), fileSystem);

        mailSender.send(mimeMessage);

    }
}
