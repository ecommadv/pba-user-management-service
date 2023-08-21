package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.repository.mappers.ActiveUserProfileRowMapper;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserProfileSqlProvider;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ActiveUserProfileDaoImpl extends JdbcRepository<ActiveUserProfile, Long> implements ActiveUserProfileDao {
    private final ActiveUserProfileRowMapper activeUserProfileRowMapper;
    private final ActiveUserProfileSqlProvider activeUserProfileSqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public ActiveUserProfileDaoImpl(ActiveUserProfileRowMapper rowMapper, ActiveUserProfileSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.activeUserProfileRowMapper = rowMapper;
        this.activeUserProfileSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ActiveUserProfile> getByUserId(Long id) {
        String sql = activeUserProfileSqlProvider.selectByUserId();
        return jdbcTemplate.query(sql, activeUserProfileRowMapper, id).stream().findFirst();
    }

    @Override
    public Optional<ActiveUserProfile> getByEmail(String email) {
        String sql = activeUserProfileSqlProvider.selectByEmail();
        return jdbcTemplate.query(sql, activeUserProfileRowMapper, email).stream().findFirst();
    }
}
