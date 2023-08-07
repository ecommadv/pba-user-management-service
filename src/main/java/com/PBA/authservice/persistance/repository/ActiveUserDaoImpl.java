package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapperImpl;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ActiveUserDaoImpl extends JdbcRepository<ActiveUser, Long> implements ActiveUserDao {
    @Autowired
    public ActiveUserDaoImpl(ActiveUserRowMapper rowMapper, ActiveUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
    }
}
