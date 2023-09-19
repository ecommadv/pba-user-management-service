package com.pba.authservice.facade;

import com.pba.authservice.controller.request.LoginRequest;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.persistance.model.dtos.UserDto;

import java.util.UUID;

public interface UserFacade {
    public void registerUser(UserCreateRequest userCreateRequest);
    public UserDto getUser(String authHeader);
    public UserDto verifyUser(UUID validationCode);
    public UserDto updateUser(String authHeader, UserUpdateRequest userUpdateRequest);
    public String loginUser(LoginRequest loginRequest);
}
