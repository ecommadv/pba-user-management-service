package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUserProfile;

import java.util.List;
import java.util.Optional;

public interface PendingUserProfileDao {
    public PendingUserProfile save(PendingUserProfile pendingUserProfile);
    public Optional<PendingUserProfile> getById(Long id);
    public List<PendingUserProfile> getAll();
    public PendingUserProfile deleteById(Long id);
    public PendingUserProfile update(PendingUserProfile pendingUserProfile, Long id);
    public Optional<PendingUserProfile> getByUserId(Long id);
    public Optional<PendingUserProfile> getByEmail(String email);
}
