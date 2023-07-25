package com.pba.authservice.service;

import com.pba.authservice.persistance.model.User;
import com.pba.authservice.persistance.model.UserDtoMapper;
import com.pba.authservice.persistance.model.UserRequest;
import com.pba.authservice.persistance.model.UserResponse;
import com.pba.authservice.persistance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserDtoMapper userDtoMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
    }

    public UserResponse addUser(UserRequest userRequest) {
        User user = userDtoMapper.fromUserRequestToUser(userRequest);
        User userSaved = userRepository.save(user);
        UserResponse userResponse = userDtoMapper.fromUserToUserResponse(userSaved);
        return userResponse;
    }

    public List<UserResponse> findAllUsers() {
        List<User> users = userRepository.findAll();
        return userDtoMapper.fromUserToUserResponse(users);
    }
}
