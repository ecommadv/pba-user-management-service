package com.pba.authservice.security;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.UserType;

import java.util.UUID;

public interface JwtUtils {
    public String generateAccessToken(ActiveUser user);
    public String generateAccessToken(ActiveUser user, Group group, UserType userType);
    public boolean isTokenValid(String token);
    public UUID extractUserUidFromUserToken(String token);
    public String extractUserType(String token);
    public UUID extractGroupUid(String token);
    public String extractTokenFromHeader(String authHeader);
    public UUID extractUserUidFromHeader(String authHeader);
    public UUID extractUserUidFromGroupToken(String token);
}
