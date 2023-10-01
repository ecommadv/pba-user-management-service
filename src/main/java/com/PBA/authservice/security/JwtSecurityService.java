package com.pba.authservice.security;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.UserType;

import java.util.UUID;

public interface JwtSecurityService {
    public UUID getCurrentUserUid();
    public void setCurrentUserUid(UUID uid);
    public String generateAccessToken(ActiveUser user);
    public String generateAccessToken(ActiveUser user, Group group, UserType userType);
}
