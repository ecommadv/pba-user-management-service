package com.pba.authservice.service;

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
import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void sendVerificationEmail(String to, UUID validationCode) {
        String subject = "Email verification";
        String verificationLink = String.format(environment.getProperty("spring.mail.verification_link"), validationCode);

        String body = """
                Click the following button to verify your account:
                
                %s
                """;

        this.sendEmailFromTemplate(to, subject, body, verificationLink);
    }

    private void sendEmailFromTemplate(String to, String subject, String body, String verificationLink) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);

            File htmlFile = ResourceUtils.getFile("classpath:email_content.html");
            String htmlTemplate = readFile(htmlFile);

            htmlTemplate = htmlTemplate.replace("${verification_link}", verificationLink);

            message.setContent(String.format(body, htmlTemplate), "text/html; charset=utf-8");
        }
        catch(Exception ex) {
            logger.error("Error when handling email message", ex);
        }

        mailSender.send(message);
    }

    private String readFile(File file) {
        StringBuilder content = new StringBuilder();
        String line;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((line = br.readLine()) != null){
                content.append(line);
            }
            br.close();
        }
        catch(Exception ex) {
            logger.error("Error when handling email_content.html file", ex);
        }

        return content.toString();
    }
}
