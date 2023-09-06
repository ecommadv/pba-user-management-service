package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.UserType;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserTypeRowMapperImpl implements UserTypeRowMapper {
    @Override
    public UserType mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserType.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
