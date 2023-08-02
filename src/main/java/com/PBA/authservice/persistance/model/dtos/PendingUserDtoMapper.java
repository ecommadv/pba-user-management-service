package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.PendingUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PendingUserDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.sql.Timestamp.from(java.time.Instant.now()))")
    @Mapping(target = "validationCode", expression = "java(java.util.UUID.randomUUID())")
    public PendingUser fromPendingUserRequestToPendingUser(PendingUserRequest pendingUserRequest);

    public PendingUserResponse fromPendingUserToPendingUserResponse(PendingUser pendingUserResult);
}
