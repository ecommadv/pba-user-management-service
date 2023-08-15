package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.PendingUser;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class PendingUserRowMapperImpl implements PendingUserRowMapper {
    @Override
    public PendingUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PendingUser.builder()
                .id(rs.getInt("id"))
                .uid(UUID.fromString(rs.getString("uid")))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .validationCode(UUID.fromString(rs.getString("validation_code")))
                .build();
    }
}
