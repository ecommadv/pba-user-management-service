package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.UserType;

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
    public ActiveUser findByUsernameAndPassword(String username, String password);
}
