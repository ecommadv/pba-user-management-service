package com.pba.authservice.controller;

import com.pba.authservice.controller.request.UserUpdateRequest;
import com.pba.authservice.facade.UserFacade;
import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.dtos.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class UserControllerImpl implements UserController {
    private final UserFacade userFacade;

    public UserControllerImpl(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public ResponseEntity<Void> registerUser(UserCreateRequest userCreateRequest) {
        userFacade.registerUser(userCreateRequest);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDto> getActiveUser(UUID uid) {
        UserDto userDto = userFacade.getUser(uid);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDto> activateUser(UUID validationCode) {
        UserDto userDto = userFacade.verifyUser(validationCode);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDto> updateUser(UUID userUid, UserUpdateRequest userUpdateRequest) {
        UserDto userDto = userFacade.updateUser(userUid, userUpdateRequest);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
