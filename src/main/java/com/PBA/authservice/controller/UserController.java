package com.pba.authservice.controller;

import com.pba.authservice.persistance.model.UserResponse;
import com.pba.authservice.persistance.model.UserRequest;
import com.pba.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.addUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUsers() {
        List<UserResponse> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
