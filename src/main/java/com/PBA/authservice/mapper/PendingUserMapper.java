package com.pba.authservice.mapper;

import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.PendingUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PendingUserMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "validationCode", expression = "java(java.util.UUID.randomUUID())")
    public PendingUser toPendingUser(PendingUserCreateRequest pendingUserRequest);
}
