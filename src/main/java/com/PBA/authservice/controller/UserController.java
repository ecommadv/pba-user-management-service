package com.pba.authservice.controller;

import com.pba.authservice.controller.request.*;
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
    @Operation(summary = "Registers an user to the application.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
            })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to register")
            @Valid @RequestBody UserCreateRequest userCreateRequest);

    @Operation(summary = "Retrieves from the system the current logged in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping
    public ResponseEntity<UserDto> getActiveUser();

    @Operation(summary = "Validates the user with the given validation code, if they exist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/activate/{code}")
    public ResponseEntity<UserDto> activateUser(@PathVariable("code") UUID validationCode);

    @Operation(summary = "Updates the user with the given uid, if they exist, and persists the new data in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest);

    @Operation(summary = "Logins an user to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest);

    @Operation(summary = "Sends an email to the requested email address with a valid token that can be used to change the user password, if the user with the requested email exists in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest);

    @Operation(summary = "Changes the password of the user that the provided token corresponds to.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/change-password/{token}")
    public ResponseEntity<Void> changePassword(@PathVariable("token") UUID token,
                                               @Valid @RequestBody ChangePasswordRequest changePasswordRequest);
}
