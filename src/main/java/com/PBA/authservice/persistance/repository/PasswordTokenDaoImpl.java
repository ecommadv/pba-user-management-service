package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PasswordToken;
import com.pba.authservice.persistance.repository.mappers.PasswordTokenRowMapper;
import com.pba.authservice.persistance.repository.sql.PasswordTokenSqlProvider;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PasswordTokenDaoImpl extends JdbcRepository<PasswordToken, Long> implements PasswordTokenDao {
    private final PasswordTokenRowMapper rowMapper;
    private final PasswordTokenSqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public PasswordTokenDaoImpl(PasswordTokenRowMapper rowMapper, PasswordTokenSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PasswordToken> getByToken(UUID token) {
        String sql = sqlProvider.selectByToken();
        return jdbcTemplate.query(sql, rowMapper, token).stream().findFirst();
    }
}
