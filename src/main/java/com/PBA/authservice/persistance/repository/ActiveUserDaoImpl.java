package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ActiveUserDaoImpl extends JdbcRepository<ActiveUser, Long> implements ActiveUserDao {
    private final ActiveUserRowMapper activeUserRowMapper;
    private final ActiveUserSqlProvider activeUserSqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public ActiveUserDaoImpl(ActiveUserRowMapper rowMapper, ActiveUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.activeUserRowMapper = rowMapper;
        this.activeUserSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ActiveUser> getByUid(UUID uid) {
        String sql = activeUserSqlProvider.selectByUid();
        return jdbcTemplate.query(sql, activeUserRowMapper, uid).stream().findFirst();
    }

    @Override
    public Optional<ActiveUser> getByUsername(String username) {
        String sql = activeUserSqlProvider.selectByUsername();
        return jdbcTemplate.query(sql, activeUserRowMapper, username).stream().findFirst();
    }

    @Override
    public Optional<ActiveUser> findByUsernameAndPassword(String username, String password) {
        String sql = activeUserSqlProvider.selectByUsernameAndPassword();
        return jdbcTemplate.query(sql, activeUserRowMapper, username, password).stream().findFirst();
    }

    @Override
    public Optional<ActiveUser> getByEmail(String email) {
        String sql = activeUserSqlProvider.selectByEmail();
        return jdbcTemplate.query(sql, activeUserRowMapper, email).stream().findFirst();
    }

    @Override
    public Optional<ActiveUser> getByPasswordToken(UUID token) {
        String sql = activeUserSqlProvider.selectByPasswordToken();
        return jdbcTemplate.query(sql, activeUserRowMapper, token).stream().findFirst();
    }
}
