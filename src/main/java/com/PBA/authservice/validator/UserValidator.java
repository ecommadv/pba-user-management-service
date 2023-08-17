package com.pba.authservice.validator;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.PendingUser;

public interface UserValidator {
    public void validateUserCreateRequest(UserCreateRequest userCreateRequest);
    public void validatePendingUser(PendingUser pendingUser);
}
