package com.pba.authservice.facade;

import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;

import java.util.UUID;

public interface UserFacade {
    public void addPendingUser(PendingUserCreateRequest pendingUserRequest);
    public ActiveUserDto getActiveUser(UUID uid);
}
