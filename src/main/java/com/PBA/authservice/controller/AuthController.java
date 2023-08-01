package com.pba.authservice.controller;

import com.pba.authservice.facade.PendingUserFacade;
import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import com.pba.authservice.persistance.model.dtos.PendingUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final PendingUserFacade pendingUserFacade;

    @Autowired
    public AuthController(PendingUserFacade pendingUserFacade) {
        this.pendingUserFacade = pendingUserFacade;
    }

    @PostMapping("/register")
    public ResponseEntity<PendingUserResponse> registerUser(@RequestBody PendingUserRequest pendingUserRequest) {
        PendingUserResponse pendingUserResponse = pendingUserFacade.addPendingUser(pendingUserRequest);
        return new ResponseEntity<>(pendingUserResponse, HttpStatus.CREATED);
    }
}
