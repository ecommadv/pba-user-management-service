package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.repository.mappers.UserTypeRowMapper;
import com.pba.authservice.persistance.repository.sql.UserTypeSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserTypeDaoImpl extends JdbcRepository<UserType, Long> implements UserTypeDao {
    private final UserTypeRowMapper rowMapper;
    private final UserTypeSqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public UserTypeDaoImpl(UserTypeRowMapper rowMapper, UserTypeSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserType> getByName(String name) {
        String sql = sqlProvider.selectByName();
        return jdbcTemplate.query(sql, rowMapper, name).stream().findFirst();
    }
}
