package com.pba.authservice.unit;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.exceptions.EntityAlreadyExistsException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.service.ActiveUserService;
import com.pba.authservice.service.PendingUserService;
import com.pba.authservice.validator.UserRequestValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class UserRequestValidatorUnitTest {
    @InjectMocks
    private UserRequestValidatorImpl userRequestValidator;

    @Mock
    private ActiveUserService activeUserService;

    @Mock
    private PendingUserService pendingUserService;

    @Test
    public void testValidateUserDoesNotAlreadyExistWhenCreate_whenUserDoesNotExist() {
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        when(activeUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(false);
        when(activeUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(false);
        when(pendingUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(false);
        when(pendingUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(false);

        assertDoesNotThrow(() -> userRequestValidator.validateUserDoesNotAlreadyExistWhenCreate(userCreateRequest));
    }

    @Test
    public void testValidateUserDoesNotAlreadyExistWhenCreate_whenUserWithUsernameExists() {
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        when(activeUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(true);
        when(activeUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(false);
        when(pendingUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(false);
        when(pendingUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> userRequestValidator.validateUserDoesNotAlreadyExistWhenCreate(userCreateRequest))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage(String.format("User with username %s already exists in the system", userCreateRequest.getUsername()));
    }

    @Test
    public void testValidateUserDoesNotAlreadyExistWhenCreate_whenUserWithEmailExists() {
        UserCreateRequest userCreateRequest = PendingUserMockGenerator.generateMockUserCreateRequest();
        when(activeUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(false);
        when(activeUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(true);
        when(pendingUserService.userWithUsernameExists(userCreateRequest.getUsername())).thenReturn(false);
        when(pendingUserService.userWithEmailExists(userCreateRequest.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> userRequestValidator.validateUserDoesNotAlreadyExistWhenCreate(userCreateRequest))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage(String.format("User with email %s already exists in the system", userCreateRequest.getEmail()));
    }

    @Test
    public void testValidateUserDoesNotAlreadyExistWhenUpdate_whenUserDoesNotExist() {
        UserUpdateRequest userUpdateRequest = ActiveUserMockGenerator.generateMockUserUpdateRequest();
        ActiveUser userToUpdate = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUserProfile profileToUpdate = ActiveUserMockGenerator.generateMockActiveUserProfile();
        when(activeUserService.userWithUsernameExists(userUpdateRequest.getUsername())).thenReturn(false);
        when(pendingUserService.userWithUsernameExists(userUpdateRequest.getUsername())).thenReturn(false);

        assertDoesNotThrow(() -> userRequestValidator.validateUserDoesNotAlreadyExistWhenUpdate(userUpdateRequest, userToUpdate, profileToUpdate));
    }

    @Test
    public void testValidateUserDoesNotAlreadyExistWhenUpdate_whenUserWithUsernameExists() {
        UserUpdateRequest userUpdateRequest = ActiveUserMockGenerator.generateMockUserUpdateRequest();
        ActiveUser userToUpdate = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUserProfile profileToUpdate = ActiveUserMockGenerator.generateMockActiveUserProfile();
        when(activeUserService.userWithUsernameExists(userUpdateRequest.getUsername())).thenReturn(true);
        when(pendingUserService.userWithUsernameExists(userUpdateRequest.getUsername())).thenReturn(false);

        assertThatThrownBy(() -> userRequestValidator.validateUserDoesNotAlreadyExistWhenUpdate(userUpdateRequest, userToUpdate, profileToUpdate))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage(String.format("User with username %s already exists in the system", userUpdateRequest.getUsername()));
    }
}
