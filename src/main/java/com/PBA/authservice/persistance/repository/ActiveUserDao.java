package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface ActiveUserDao {
    public ActiveUser save(ActiveUser activeUser);
    public Optional<ActiveUser> getById(Long id);
    public List<ActiveUser> getAll();
    public ActiveUser deleteById(Long id);
    public ActiveUser update(ActiveUser activeUser, Long id);
    public Optional<ActiveUser> getByUid(UUID uid);
    public Optional<ActiveUser> getByUsername(String username);
    public Optional<ActiveUser> findByUsernameAndPassword(String username, String password);
}
