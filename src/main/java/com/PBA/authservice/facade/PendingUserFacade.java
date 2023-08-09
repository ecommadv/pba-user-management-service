package com.pba.authservice.facade;

import com.pba.authservice.persistance.model.dtos.PendingUserRequest;

public interface PendingUserFacade {
    public void addPendingUser(PendingUserRequest pendingUserRequest);
}
