package com.pba.authservice.mapper;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.model.dtos.UserDto;
import com.pba.authservice.persistance.model.dtos.UserProfileDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ActiveUserMapper {
    @Mapping(target = "userProfile", expression = "java(userProfileDto)")
    public UserDto toUserDto(ActiveUser activeUser, UserProfileDto userProfileDto);

    public UserProfileDto toUserProfileDto(ActiveUserProfile activeUserProfile);
}
