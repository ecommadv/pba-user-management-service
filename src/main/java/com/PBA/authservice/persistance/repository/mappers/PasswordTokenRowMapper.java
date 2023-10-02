package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.PasswordToken;
import org.springframework.jdbc.core.RowMapper;

public interface PasswordTokenRowMapper extends RowMapper<PasswordToken> {
}
