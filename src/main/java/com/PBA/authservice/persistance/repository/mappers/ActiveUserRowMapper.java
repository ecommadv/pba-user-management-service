package com.pba.authservice.persistance.repository.mappers;

import com.pba.authservice.persistance.model.ActiveUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ActiveUserRowMapper extends RowMapper<ActiveUser> {
    public ActiveUser mapRow(ResultSet rs, int rowNum) throws SQLException;
}
