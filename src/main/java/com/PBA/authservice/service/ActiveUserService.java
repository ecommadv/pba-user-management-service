package com.pba.authservice.service;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;

import java.util.UUID;

public interface ActiveUserService {
    public ActiveUser addUser(ActiveUser activeUser);
    public ActiveUser getUserByUid(UUID uid);
    public ActiveUserProfile getProfileByUserId(Long id);
    public ActiveUserProfile addUserProfile(ActiveUserProfile activeUserProfile);
}
