package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.Group;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupDao {
    public Group save(Group group);
    public Optional<Group> getById(Long id);
    public List<Group> getAll();
    public Group deleteById(Long id);
    public Group update(Group group, Long id);
    public Optional<Group> getByName(String name);
    public Optional<Group> getByUid(UUID uid);
}
