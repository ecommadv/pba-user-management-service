package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PasswordToken;

import java.util.UUID;

public interface EmailService {
    public void sendVerificationEmail(String to, UUID validationCode);
    public void sendForgotPasswordEmail(String to, PasswordToken passwordToken);
}
