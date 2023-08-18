package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.PendingUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface PendingUserRowMapper extends RowMapper<PendingUser> {
    public PendingUser mapRow(ResultSet rs, int rowNum) throws SQLException;
}
