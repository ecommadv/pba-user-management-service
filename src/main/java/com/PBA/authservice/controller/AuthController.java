package com.pba.authservice.controller;

import com.pba.authservice.persistance.model.dtos.PendingUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

public interface AuthController {
    @Operation(summary = """
            Registers an user to the application. Uses the PendingUserRequest request body to persist data required to create a pending user.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
            })
    public ResponseEntity<?> registerUser(
            @RequestBody(description = "User to register", required = true,
                        content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PendingUserRequest.class)) })
             PendingUserRequest pendingUserRequest);
}
