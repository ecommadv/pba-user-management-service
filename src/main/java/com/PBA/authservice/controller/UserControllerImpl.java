package com.pba.authservice.controller;

import com.pba.authservice.facade.UserFacade;
import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
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
    public ResponseEntity<Void> registerUser(PendingUserCreateRequest pendingUserRequest) {
        userFacade.addPendingUser(pendingUserRequest);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ActiveUserDto> getActiveUser(UUID uid) {
        ActiveUserDto activeUserDto = userFacade.getActiveUser(uid);
        return new ResponseEntity<>(activeUserDto, HttpStatus.OK);
    }
}
