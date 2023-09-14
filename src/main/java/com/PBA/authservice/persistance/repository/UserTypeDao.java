package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeDao {
    public UserType save(UserType userType);
    public Optional<UserType> getById(Long id);
    public List<UserType> getAll();
    public UserType deleteById(Long id);
    public UserType update(UserType userType, Long id);
    public Optional<UserType> getByName(String name);
}
