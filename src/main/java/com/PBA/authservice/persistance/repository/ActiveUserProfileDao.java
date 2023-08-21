package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUserProfile;

import java.util.List;
import java.util.Optional;

public interface ActiveUserProfileDao {
    public ActiveUserProfile save(ActiveUserProfile activeUserProfile);
    public Optional<ActiveUserProfile> getById(Long id);
    public List<ActiveUserProfile> getAll();
    public ActiveUserProfile deleteById(Long id);
    public ActiveUserProfile update(ActiveUserProfile activeUserProfile, Long id);
    public Optional<ActiveUserProfile> getByUserId(Long id);
    public Optional<ActiveUserProfile> getByEmail(String email);
}
