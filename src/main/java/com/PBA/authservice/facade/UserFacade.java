package com.pba.authservice.facade;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.dtos.UserDto;

import java.util.UUID;

public interface UserFacade {
    public void registerUser(UserCreateRequest userCreateRequest);
    public UserDto getUser(UUID uid);
}
