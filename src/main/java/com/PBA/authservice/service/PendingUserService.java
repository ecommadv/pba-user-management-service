package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;

import java.util.UUID;

public interface PendingUserService {
    public PendingUser addPendingUser(PendingUser pendingUser);
    public PendingUserProfile addPendingUserProfile(PendingUserProfile pendingUserProfile);
    public PendingUser getPendingUserByValidationCode(UUID validationCode);
    public PendingUser deletePendingUserById(Long id);
    public PendingUserProfile getPendingUserProfileByUserId(Long id);
    public PendingUserProfile deletePendingProfileById(Long id);
    public boolean userWithEmailExists(String email);
    public boolean userWithUsernameExists(String username);
}
