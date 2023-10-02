package com.pba.authservice.facade;

import com.pba.authservice.controller.request.*;
import com.pba.authservice.persistance.model.dtos.UserDto;

import java.util.UUID;

public interface UserFacade {
    public void registerUser(UserCreateRequest userCreateRequest);
    public UserDto getUser();
    public UserDto verifyUser(UUID validationCode);
    public UserDto updateUser(UserUpdateRequest userUpdateRequest);
    public String loginUser(LoginRequest loginRequest);
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
    public void changePassword(UUID token, ChangePasswordRequest changePasswordRequest);
}
