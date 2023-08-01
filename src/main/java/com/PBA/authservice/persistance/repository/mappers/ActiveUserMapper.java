package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.ActiveUser;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class ActiveUserMapper implements RowMapper<ActiveUser> {
    @Override
    public ActiveUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ActiveUser.builder()
                .id(rs.getInt("id"))
                .uid(UUID.fromString(rs.getString("uid")))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .build();
    }
}
