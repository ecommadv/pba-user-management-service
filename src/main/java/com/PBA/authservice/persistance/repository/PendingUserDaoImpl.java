package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PendingUserDaoImpl extends JdbcRepository<PendingUser, Long> implements PendingUserDao {
    public PendingUserDaoImpl(PendingUserRowMapper rowMapper, PendingUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
    }

}
