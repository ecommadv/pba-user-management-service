package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.GroupMember;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMemberRowMapperImpl implements GroupMemberRowMapper {
    @Override
    public GroupMember mapRow(ResultSet rs, int rowNum) throws SQLException {
        return GroupMember.builder()
                .id(rs.getLong("id"))
                .userId(rs.getLong("user_id"))
                .userTypeId(rs.getLong("user_type_id"))
                .groupId(rs.getLong("group_id"))
                .build();
    }
}
