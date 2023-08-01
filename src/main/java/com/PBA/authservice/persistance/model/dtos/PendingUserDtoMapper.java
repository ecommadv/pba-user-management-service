package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Mapper
public abstract class PendingUserDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
    @Mapping(target = "validationCode", expression = "java(java.util.UUID.randomUUID())")
    public abstract PendingUser fromPendingUserRequestToPendingUser(PendingUserRequest pendingUserRequest);

    public abstract PendingUserResponse fromPendingUserToPendingUserResponse(PendingUser pendingUserResult);
}
