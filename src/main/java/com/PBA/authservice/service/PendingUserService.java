package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;

public interface PendingUserService {
    public PendingUser addPendingUser(PendingUser pendingUser);
    public PendingUserProfile addPendingUserProfile(PendingUserProfile pendingUserProfile);
}
