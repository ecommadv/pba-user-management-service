package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PendingUserDaoImpl extends JdbcRepository<PendingUser, Long> implements PendingUserDao {
    private final PendingUserRowMapper pendingUserRowMapper;
    private final PendingUserSqlProvider pendingUserSqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public PendingUserDaoImpl(PendingUserRowMapper rowMapper, PendingUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.pendingUserRowMapper = rowMapper;
        this.pendingUserSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PendingUser> getByValidationCode(UUID validationCode) {
        String sql = pendingUserSqlProvider.selectByValidationCode();
        return jdbcTemplate.query(sql, pendingUserRowMapper, validationCode).stream().findFirst();
    }

    @Override
    public Optional<PendingUser> getByUsername(String username) {
        String sql = pendingUserSqlProvider.selectByUsername();
        return jdbcTemplate.query(sql, pendingUserRowMapper, username).stream().findFirst();
    }
}
