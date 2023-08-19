package com.pba.authservice.mapper;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PendingUserMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "validationCode", expression = "java(java.util.UUID.randomUUID())")
    public PendingUser toPendingUser(UserCreateRequest userCreateRequest);

    @Mapping(target = "userId", expression = "java(userId)")
    public PendingUserProfile toPendingUserProfile(UserCreateRequest userCreateRequest, Long userId);

    @Mapping(target = "id", ignore = true)
    public ActiveUser toActiveUser(PendingUser pendingUser);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", expression = "java(userId)")
    public ActiveUserProfile toActiveUserProfile(PendingUserProfile pendingUserProfile, Long userId);
}
