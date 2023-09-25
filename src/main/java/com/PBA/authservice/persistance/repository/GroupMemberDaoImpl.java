package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.repository.mappers.GroupMemberRowMapper;
import com.pba.authservice.persistance.repository.sql.GroupMemberSqlProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GroupMemberDaoImpl extends JdbcRepository<GroupMember, Long> implements GroupMemberDao {
    private final GroupMemberRowMapper rowMapper;
    private final GroupMemberSqlProvider sqlProvider;
    private final JdbcTemplate jdbcTemplate;

    public GroupMemberDaoImpl(GroupMemberRowMapper rowMapper, GroupMemberSqlProvider sqlProvider, JdbcTemplate jdbcTemplate, UtilsFactory utilsFactory) {
        super(rowMapper, sqlProvider, jdbcTemplate, utilsFactory);
        this.rowMapper = rowMapper;
        this.sqlProvider = sqlProvider;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<GroupMember> getByUserIdAndGroupId(Long userId, Long groupId) {
        String sql = sqlProvider.selectByUserIdAndGroupId();
        return jdbcTemplate.query(sql, rowMapper, userId, groupId).stream().findFirst();
    }
}
