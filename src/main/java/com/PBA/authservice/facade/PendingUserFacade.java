package com.pba.authservice.facade;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.dtos.PendingUserDtoMapper;
import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import com.pba.authservice.persistance.model.dtos.PendingUserResponse;
import com.pba.authservice.service.PendingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PendingUserFacade {
    private final PendingUserService pendingUserService;
    private final PendingUserDtoMapper pendingUserDtoMapper;
    @Autowired
    public PendingUserFacade(PendingUserService pendingUserService, PendingUserDtoMapper pendingUserDtoMapper) {
        this.pendingUserService = pendingUserService;
        this.pendingUserDtoMapper = pendingUserDtoMapper;
    }

    public PendingUserResponse addPendingUser(PendingUserRequest pendingUserRequest) {
        PendingUser pendingUser = pendingUserDtoMapper.fromPendingUserRequestToPendingUser(pendingUserRequest);
        PendingUser pendingUserResult = pendingUserService.addPendingUser(pendingUser);
        return pendingUserDtoMapper.fromPendingUserToPendingUserResponse(pendingUserResult);
    }
}
