package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.ActiveUserProfile;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ActiveUserProfileRowMapperImpl implements ActiveUserProfileRowMapper {
    @Override
    public ActiveUserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ActiveUserProfile.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("firstname"))
                .lastName(rs.getString("lastname"))
                .email(rs.getString("email"))
                .country(rs.getString("country"))
                .age(rs.getInt("age"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}
