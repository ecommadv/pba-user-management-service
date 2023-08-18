package com.pba.authservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, UUID validationCode) {
        String subject = "Email verification";
        String verificationLink = String.format("http://localhost:8080/api/user/%s", validationCode);

        String htmlContent = String.format("""
                <form id="verificationForm" action="%s" method="post">
                    <input type="submit" value="Verify account">
                </form>
                <script>
                    document.getElementById('verificationForm').submit();
                </script>
                """, verificationLink);
        String body = String.format("""
                Click the following button to verify your account:
                
                %s
                """, htmlContent);

        this.sendHtmlEmail(to, subject, body);
    }

    private void sendHtmlEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
