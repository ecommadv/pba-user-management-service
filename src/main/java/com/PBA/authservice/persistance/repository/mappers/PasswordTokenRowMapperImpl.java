package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.PasswordToken;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class PasswordTokenRowMapperImpl implements PasswordTokenRowMapper {
    @Override
    public PasswordToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PasswordToken.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .token(UUID.fromString(rs.getString("token")))
                .build();
    }
}
