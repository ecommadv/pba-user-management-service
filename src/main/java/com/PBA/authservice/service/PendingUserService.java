package com.pba.authservice.service;

import com.pba.authservice.persistance.model.PendingUser;

public interface PendingUserService {
    public PendingUser addPendingUser(PendingUser pendingUser);
}
