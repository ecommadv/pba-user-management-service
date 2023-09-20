package com.pba.authservice.security;

import com.pba.authservice.persistance.model.ActiveUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtSecurityServiceImpl implements JwtSecurityService {
    private final SecurityContextHolder contextHolder;
    private final JwtUtils jwtUtils;

    public JwtSecurityServiceImpl(SecurityContextHolder contextHolder, JwtUtils jwtUtils) {
        this.contextHolder = contextHolder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void setCurrentUserUid(UUID uid) {
        contextHolder.setCurrentUserUid(uid);
    }

    @Override
    public String generateAccessToken(ActiveUser user) {
        return jwtUtils.generateAccessToken(user);
    }

    @Override
    public UUID getCurrentUserUid() {
        return contextHolder.getCurrentUserUid();
    }
}
