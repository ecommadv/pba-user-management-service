package com.pba.authservice.service;

import com.pba.authservice.exceptions.EmailNotSentException;
import com.pba.authservice.exceptions.ErrorCodes;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    private JavaMailSender mailSender;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.verification_link}")
    private String genericVerificationLink;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, UUID validationCode) {
        String subject = "Email verification";
        String verificationLink = String.format(genericVerificationLink, validationCode);

        this.sendEmailFromTemplate(to, subject, verificationLink);
    }

    private void sendEmailFromTemplate(String to, String subject, String verificationLink) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);

            File htmlFile = ResourceUtils.getFile("classpath:email_content.html");
            String htmlTemplate = readFile(htmlFile);

            htmlTemplate = htmlTemplate.replace("${verification_link}", verificationLink);

            message.setContent(htmlTemplate, "text/html; charset=utf-8");
        }
        catch(Exception ex) {
            throw new EmailNotSentException(ErrorCodes.EMAIL_NOT_SENT, "An error occurred when trying to send an email for user activation");
        }

        mailSender.send(message);
    }

    private String readFile(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }
}
