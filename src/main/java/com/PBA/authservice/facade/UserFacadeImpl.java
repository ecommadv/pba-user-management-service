package com.pba.authservice.facade;

import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import com.pba.authservice.controller.request.PendingUserCreateRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserFacadeImpl implements UserFacade {
    private final PendingUserService pendingUserService;
    private final PendingUserMapper pendingUserMapper;
    private final ActiveUserService activeUserService;
    private final ActiveUserMapper activeUserMapper;

    public UserFacadeImpl(PendingUserService pendingUserService, PendingUserMapper pendingUserMapper, ActiveUserService activeUserService, ActiveUserMapper activeUserMapper) {
        this.pendingUserService = pendingUserService;
        this.pendingUserMapper = pendingUserMapper;
        this.activeUserService = activeUserService;
        this.activeUserMapper = activeUserMapper;
    }

    @Override
    public void addPendingUser(PendingUserCreateRequest pendingUserRequest) {
        PendingUser pendingUser = pendingUserMapper.toPendingUser(pendingUserRequest);
        pendingUserService.addPendingUser(pendingUser);
    }

    @Override
    public ActiveUserDto getActiveUser(UUID uid) {
        ActiveUser activeUser = activeUserService.getByUid(uid);
        return activeUserMapper.toActiveUserDto(activeUser);
    }
}
