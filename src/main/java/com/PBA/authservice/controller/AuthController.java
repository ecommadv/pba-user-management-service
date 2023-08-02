package com.pba.authservice.controller;

import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import com.pba.authservice.persistance.model.dtos.PendingUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {
    public ResponseEntity<PendingUserResponse> registerUser(PendingUserRequest pendingUserRequest);
}
