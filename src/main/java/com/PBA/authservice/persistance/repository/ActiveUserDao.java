package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ActiveUserDao {
    public ActiveUser save(ActiveUser activeUser);
    public Optional<ActiveUser> getById(UUID uid);
    public List<ActiveUser> getAll();
    public ActiveUser deleteById(UUID uid);
    public ActiveUser update(ActiveUser activeUser);
}
