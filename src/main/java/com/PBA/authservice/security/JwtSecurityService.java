package com.pba.authservice.security;

import com.pba.authservice.persistance.model.ActiveUser;

import java.util.UUID;

public interface JwtSecurityService {
    public UUID getCurrentUserUid();
    public void setCurrentUserUid(UUID uid);
    public String generateAccessToken(ActiveUser user);
}
