package com.pba.authservice.controller;

import com.pba.authservice.controller.request.UserCreateRequest;
import com.pba.authservice.persistance.model.dtos.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
            })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register")
            @Valid @RequestBody UserCreateRequest userCreateRequest);

    @Operation(summary = """
            Retrieves from the system the active user with the specified uid, if they exist in the system.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getActiveUser(@PathVariable("uid") UUID uid);

    @Operation(summary = """
            Validates the user with the given validation code, if they exist.
            """)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/activate/{code}")
    public ResponseEntity<UserDto> activateUser(@PathVariable("code") UUID validationCode);
}
