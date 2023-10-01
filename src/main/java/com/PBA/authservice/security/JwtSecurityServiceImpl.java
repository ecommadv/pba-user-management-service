package com.pba.authservice.security;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.UserType;
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
    public String generateAccessToken(ActiveUser user, Group group, UserType userType) {
        return jwtUtils.generateAccessToken(user, group, userType);
    }

    @Override
    public void setCurrentUserType(UserType userType) {
        contextHolder.setCurrentUserType(userType);
    }

    @Override
    public UserType getCurrentUserType() {
        return contextHolder.getCurrentUserType();
    }

    @Override
    public void setCurrentGroupUid(UUID groupUid) {
        contextHolder.setCurrentGroupUid(groupUid);
    }

    @Override
    public UUID getCurrentGroupUid() {
        return contextHolder.getCurrentGroupUid();
    }

    @Override
    public UUID getCurrentUserUid() {
        return contextHolder.getCurrentUserUid();
    }
}
