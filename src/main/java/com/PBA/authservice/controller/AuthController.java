package com.pba.authservice.controller;

import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public interface AuthController {
    @Operation(summary = """
            Registers an user to the application. Uses the PendingUserRequest request body to persist data required to create a pending user.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
            })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register")
            @RequestBody PendingUserRequest pendingUserRequest);
}
