package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;

import java.util.UUID;

public interface JwtService {
    public String generateAccessToken(ActiveUser user);
    public boolean isTokenValid(String token, ActiveUser activeUser);
    public UUID extractUserUid(String token);
    public String extractTokenFromHeader(String authHeader);
    public UUID extractUserUidFromHeader(String authHeader);
}
