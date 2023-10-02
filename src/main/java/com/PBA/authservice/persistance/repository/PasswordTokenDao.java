package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PasswordToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PasswordTokenDao {
    public PasswordToken save(PasswordToken passwordToken);
    public Optional<PasswordToken> getById(Long id);
    public List<PasswordToken> getAll();
    public PasswordToken deleteById(Long id);
    public PasswordToken update(PasswordToken passwordToken, Long id);
    public Optional<PasswordToken> getByToken(UUID token);
}
