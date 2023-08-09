package com.pba.authservice.persistance.model.dtos;

import com.pba.authservice.persistance.model.PendingUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper
public interface PendingUserDtoMapper {
    @Mapping(target = "uid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "validationCode", expression = "java(java.util.UUID.randomUUID())")
    public PendingUser toPendingUser(PendingUserRequest pendingUserRequest);
}
