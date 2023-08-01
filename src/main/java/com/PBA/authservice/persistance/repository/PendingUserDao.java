package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.mappers.PendingUserMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import com.pba.authservice.persistance.repository.sql.SqlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PendingUserDao extends JdbcRepository<PendingUser, UUID> {
    @Autowired
    public PendingUserDao(PendingUserMapper rowMapper, PendingUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate);
    }
    public PendingUser save(PendingUser pendingUser) {
        return super.save(pendingUser, pendingUser.getUid());
    }

}
