package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class UserDaoMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                UUID.fromString(rs.getString("id")),
                rs.getString("firstname"),
                rs.getString("lastname"),
                rs.getString("email"),
                rs.getString("password")
        );
    }
}
