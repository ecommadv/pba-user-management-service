package com.pba.authservice.validator;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.exceptions.EntityAlreadyExistsException;
import com.pba.authservice.exceptions.ErrorCodes;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import org.springframework.stereotype.Component;

@Component
public class UserRequestValidatorImpl implements UserRequestValidator {
    private final ActiveUserService activeUserService;
    private final PendingUserService pendingUserService;

    public UserRequestValidatorImpl(ActiveUserService activeUserService, PendingUserService pendingUserService) {
        this.activeUserService = activeUserService;
        this.pendingUserService = pendingUserService;
    }

    @Override
    public void validateUserDoesNotAlreadyExistWhenCreate(UserCreateRequest userCreateRequest) {
        String requestedEmail = userCreateRequest.getEmail();
        String requestedUsername = userCreateRequest.getUsername();
        boolean userWithEmailExists = pendingUserService.userWithEmailExists(requestedEmail) || activeUserService.userWithEmailExists(requestedEmail);
        boolean userWithUsernameExists = pendingUserService.userWithUsernameExists(requestedUsername) || activeUserService.userWithUsernameExists(requestedUsername);
        String errorMessage = userWithEmailExists
                ? String.format("User with email %s already exists in the system", requestedEmail)
                : userWithUsernameExists
                    ? String.format("User with username %s already exists in the system", requestedUsername)
                    : "";
        if (!errorMessage.isBlank()) {
            throw new EntityAlreadyExistsException(ErrorCodes.USER_ALREADY_EXISTS, errorMessage);
        }
    }

    @Override
    public void validateUserDoesNotAlreadyExistWhenUpdate(UserUpdateRequest userUpdateRequest,
                                                    ActiveUser userToUpdate,
                                                    ActiveUserProfile profileToUpdate) {
        boolean requestedUsernameChange = !userUpdateRequest.getUsername().equals(userToUpdate.getUsername());
        String username = userUpdateRequest.getUsername();
        boolean userWithUsernameExists = pendingUserService.userWithUsernameExists(username) || activeUserService.userWithUsernameExists(username);
        if (requestedUsernameChange && userWithUsernameExists) {
            String errorMessage = String.format("User with username %s already exists in the system", username);
            throw new EntityAlreadyExistsException(ErrorCodes.USER_ALREADY_EXISTS, errorMessage);
        }
    }
}
