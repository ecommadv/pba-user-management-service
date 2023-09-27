package com.pba.authservice.controller;

import com.pba.authservice.controller.request.GroupCreateRequest;
import com.pba.authservice.controller.request.GroupInviteRequest;
import com.pba.authservice.controller.request.GroupLoginRequest;
import com.pba.authservice.persistance.model.dtos.GroupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public interface GroupController {
    @Operation(summary = "Creates a new group and persists it in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@Valid @RequestBody GroupCreateRequest groupCreateRequest);

    @Operation(summary = "Invites an user to a group as a regular user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteUserToGroup(@Valid @RequestBody GroupInviteRequest groupInviteRequest);

    @Operation(summary = "Allows users to log in to a group that they are part of.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginToGroup(@Valid @RequestBody GroupLoginRequest groupLoginRequest);
}
