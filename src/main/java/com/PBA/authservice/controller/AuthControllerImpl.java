package com.pba.authservice.controller;

import com.pba.authservice.facade.PendingUserFacade;
import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {
    private final PendingUserFacade pendingUserFacade;

    public AuthControllerImpl(PendingUserFacade pendingUserFacade) {
        this.pendingUserFacade = pendingUserFacade;
    }

    @Override
    public ResponseEntity<Void> registerUser(PendingUserRequest pendingUserRequest) {
        pendingUserFacade.addPendingUser(pendingUserRequest);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
}
