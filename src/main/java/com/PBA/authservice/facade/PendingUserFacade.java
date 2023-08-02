package com.pba.authservice.facade;

import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import com.pba.authservice.persistance.model.dtos.PendingUserResponse;

public interface PendingUserFacade {
    public PendingUserResponse addPendingUser(PendingUserRequest pendingUserRequest);
}
