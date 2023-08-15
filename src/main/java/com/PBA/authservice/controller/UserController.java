package com.pba.authservice.controller;

import com.pba.authservice.controller.request.PendingUserCreateRequest;
import com.pba.authservice.persistance.model.dtos.ActiveUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public interface UserController {
    @Operation(summary = """
            Registers an user to the application.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created")
            })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register")
            @RequestBody PendingUserCreateRequest pendingUserRequest);

    @Operation(summary = """
            Retrieves from the system the active user with the specified uid, if they exist in the system.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/active/{uid}")
    public ResponseEntity<ActiveUserDto> getActiveUser(@PathVariable("uid") UUID uid);
}
