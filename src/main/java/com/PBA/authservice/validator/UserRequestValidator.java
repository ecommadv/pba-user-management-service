package com.pba.authservice.validator;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;

public interface UserRequestValidator {
    public void validateUserDoesNotAlreadyExistWhenCreate(UserCreateRequest userCreateRequest);
    public void validateUserDoesNotAlreadyExistWhenUpdate(UserUpdateRequest userUpdateRequest,
                                                          ActiveUser userToUpdate,
                                                          ActiveUserProfile profileToUpdate);
}
