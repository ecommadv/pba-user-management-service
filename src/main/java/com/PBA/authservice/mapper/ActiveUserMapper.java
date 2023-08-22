package com.pba.authservice.mapper;

import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface ActiveUserMapper {
    @Mapping(target = "userProfile", expression = "java(userProfileDto)")
    public UserDto toUserDto(ActiveUser activeUser, UserProfileDto userProfileDto);

    public UserProfileDto toUserProfileDto(ActiveUserProfile activeUserProfile);

    @Mapping(target = "id", expression = "java(userId)")
    @Mapping(target = "uid", expression = "java(userUid)")
    public ActiveUser toUser(UserUpdateRequest userUpdateRequest, long userId, UUID userUid);

    @Mapping(target = "id", expression = "java(userProfileId)")
    @Mapping(target = "userId", expression = "java(userId)")
    public ActiveUserProfile toUserProfile(UserUpdateRequest userUpdateRequest, long userId, long userProfileId);
}
