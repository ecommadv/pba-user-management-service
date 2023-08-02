package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PendingUserDao {
    public PendingUser save(PendingUser pendingUser);
    public Optional<PendingUser> getById(UUID uid);
    public List<PendingUser> getAll();
    public PendingUser deleteById(UUID uid);
    public PendingUser update(PendingUser pendingUser);
}
