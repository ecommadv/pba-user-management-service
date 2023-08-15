package com.pba.authservice.mapper;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import org.mapstruct.Mapper;

@Mapper
public interface ActiveUserMapper {
    public ActiveUserDto toActiveUserDto(ActiveUser activeUser);
}
