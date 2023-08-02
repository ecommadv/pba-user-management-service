package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PendingUserDaoImpl extends JdbcRepository<PendingUser, UUID> implements PendingUserDao {
    @Autowired
    public PendingUserDaoImpl(PendingUserRowMapper rowMapper, PendingUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate);
    }
    @Override
    public PendingUser save(PendingUser pendingUser) {
        return super.save(pendingUser, pendingUser.getUid());
    }

    @Override
    public PendingUser update(PendingUser pendingUser) {
        return super.update(pendingUser, pendingUser.getUid());
    }

}
