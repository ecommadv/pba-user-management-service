package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.mappers.PendingUserProfileRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserProfileSqlProvider;
import com.pba.authservice.persistance.repository.sql.PendingUserProfileSqlProviderImpl;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PendingUserProfileDaoImpl extends JdbcRepository<PendingUserProfile, Long> implements PendingUserProfileDao {
    private final PendingUserProfileRowMapper pendingUserProfileRowMapper;
    private final PendingUserProfileSqlProvider pendingUserSqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public PendingUserProfileDaoImpl(PendingUserProfileRowMapper rowMapper, PendingUserProfileSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.pendingUserProfileRowMapper = rowMapper;
        this.pendingUserSqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<PendingUserProfile> getByUserId(Long id) {
        String sql = pendingUserSqlProvider.selectByUserId();
        return jdbcTemplate.query(sql, pendingUserProfileRowMapper, id).stream().findFirst();
    }

    @Override
    public Optional<PendingUserProfile> getByEmail(String email) {
        String sql = pendingUserSqlProvider.selectByEmail();
        return jdbcTemplate.query(sql, pendingUserProfileRowMapper, email).stream().findFirst();
    }
}
