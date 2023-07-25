package com.pba.authservice.persistance.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserDtoMapper {
    public User fromUserRequestToUser(UserRequest userRequest) {
        return User.builder()
                .id(UUID.randomUUID())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }

    public UserResponse fromUserToUserResponse(User user) {
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public List<UserResponse> fromUserToUserResponse(List<User> users) {
        return users
                .stream()
                .map(this::fromUserToUserResponse)
                .collect(Collectors.toList());
    }
}
