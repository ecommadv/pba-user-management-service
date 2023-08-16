package com.pba.authservice.facade;

import com.pba.authservice.mapper.ActiveUserMapper;
import com.pba.authservice.mapper.PendingUserMapper;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import com.pba.authservice.controller.request.UserCreateRequest;
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
    public void registerUser(UserCreateRequest userCreateRequest) {
        PendingUser pendingUser = pendingUserMapper.toPendingUser(userCreateRequest);
        PendingUser savedPendingUser = pendingUserService.addPendingUser(pendingUser);

        PendingUserProfile pendingUserProfile = pendingUserMapper.toPendingUserProfile(userCreateRequest, savedPendingUser.getId());
        pendingUserService.addPendingUserProfile(pendingUserProfile);
    }

    @Override
    public UserDto getUser(UUID uid) {
        ActiveUser activeUser = activeUserService.getUserByUid(uid);
        ActiveUserProfile activeUserProfile = activeUserService.getProfileByUserId(activeUser.getId());

        UserProfileDto userProfileDto = activeUserMapper.toUserProfileDto(activeUserProfile);
        return activeUserMapper.toUserDto(activeUser, userProfileDto);
    }
}
