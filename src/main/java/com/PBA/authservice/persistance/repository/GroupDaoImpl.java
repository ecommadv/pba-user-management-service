package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.repository.mappers.GroupRowMapper;
import com.pba.authservice.persistance.repository.sql.GroupSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupDaoImpl extends JdbcRepository<Group, Long> implements GroupDao {
    private final GroupRowMapper rowMapper;
    private final GroupSqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public GroupDaoImpl(GroupRowMapper rowMapper, GroupSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Group> getByName(String name) {
        String sql = sqlProvider.selectByName();
        return jdbcTemplate.query(sql, rowMapper, name).stream().findFirst();
    }

    @Override
    public Optional<Group> getByUid(UUID uid) {
        String sql = sqlProvider.selectByUid();
        return jdbcTemplate.query(sql, rowMapper, uid).stream().findFirst();
    }
}
