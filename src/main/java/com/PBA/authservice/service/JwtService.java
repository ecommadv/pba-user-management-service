package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;

public interface JwtService {
    public String generateAccessToken(ActiveUser user);
}
