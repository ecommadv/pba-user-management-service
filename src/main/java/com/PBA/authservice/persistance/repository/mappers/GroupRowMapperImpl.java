package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.Group;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class GroupRowMapperImpl implements GroupRowMapper {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Group.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .uid(UUID.fromString(rs.getString("uid")))
                .build();
    }
}
