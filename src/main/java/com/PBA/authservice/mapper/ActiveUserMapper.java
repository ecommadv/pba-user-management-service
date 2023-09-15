package com.pba.authservice.mapper;

import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.dtos.LoginDto;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface ActiveUserMapper {
    @Mapping(target = "userProfile", expression = "java(userProfileDto)")
    public UserDto toUserDto(ActiveUser activeUser, UserProfileDto userProfileDto);

    public UserProfileDto toUserProfileDto(ActiveUserProfile activeUserProfile);

    @Mapping(target = "id", expression = "java(activeUser.getId())")
    @Mapping(target = "uid", expression = "java(activeUser.getUid())")
    @Mapping(target = "password", expression = "java(activeUser.getPassword())")
    public ActiveUser toUser(UserUpdateRequest userUpdateRequest, @Context ActiveUser activeUser);

    @Mapping(target = "id", expression = "java(userProfile.getId())")
    @Mapping(target = "userId", expression = "java(userProfile.getUserId())")
    @Mapping(target = "email", expression = "java(userProfile.getEmail())")
    public ActiveUserProfile toUserProfile(UserUpdateRequest userUpdateRequest, @Context ActiveUserProfile userProfile);

    @Mapping(target = "user", expression = "java(userDto)")
    public LoginDto toLoginDto(UserDto userDto, String token);
}
