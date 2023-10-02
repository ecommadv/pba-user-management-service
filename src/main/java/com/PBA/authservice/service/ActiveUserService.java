package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PasswordToken;
import com.pba.authservice.persistance.model.UserType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

public interface ActiveUserService {
    public ActiveUser addUser(ActiveUser activeUser);
    public ActiveUser getUserByUid(UUID uid);
    public ActiveUserProfile getProfileByUserId(Long id);
    public ActiveUserProfile addUserProfile(ActiveUserProfile activeUserProfile);
    public boolean userWithEmailExists(String email);
    public boolean userWithUsernameExists(String username);
    public UserType getUserTypeByName(String name);
    public void updateUser(ActiveUser updatedUser, ActiveUserProfile updatedProfile);
    public ActiveUser findByUsernameAndPassword(String username, String password, PasswordEncoder passwordEncoder);
    public Optional<UserType> getUserTypeById(Long id);
    public PasswordToken addPasswordToken(PasswordToken passwordToken);
    public ActiveUser getUserByEmail(String email);
    public ActiveUser getUserByPasswordToken(UUID token);
    public void updateUser(ActiveUser user);
    public void deletePasswordToken(UUID token);
}
