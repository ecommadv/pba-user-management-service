package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.mappers.PendingUserProfileRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserProfileSqlProviderImpl;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PendingUserProfileDaoImpl extends JdbcRepository<PendingUserProfile, Long> implements PendingUserProfileDao {

    public PendingUserProfileDaoImpl(PendingUserProfileRowMapper rowMapper, PendingUserProfileSqlProviderImpl sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
    }
}
