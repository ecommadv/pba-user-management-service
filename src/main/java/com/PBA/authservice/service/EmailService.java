package com.pba.authservice.service;

import java.util.UUID;

public interface EmailService {
    public void sendVerificationEmail(String to, UUID validationCode);
}
