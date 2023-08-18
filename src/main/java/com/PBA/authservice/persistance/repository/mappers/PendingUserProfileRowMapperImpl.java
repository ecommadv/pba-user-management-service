package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.PendingUserProfile;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PendingUserProfileRowMapperImpl implements PendingUserProfileRowMapper {
    @Override
    public PendingUserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PendingUserProfile.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("firstname"))
                .lastName(rs.getString("lastname"))
                .email(rs.getString("email"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}
