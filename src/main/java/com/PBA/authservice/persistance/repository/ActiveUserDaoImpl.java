package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ActiveUserDaoImpl extends JdbcRepository<ActiveUser, UUID> implements ActiveUserDao {
    @Autowired
    public ActiveUserDaoImpl(ActiveUserRowMapper rowMapper, ActiveUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate);
    }

    @Override
    public ActiveUser save(ActiveUser activeUser) {
        return super.save(activeUser, activeUser.getUid());
    }

    @Override
    public ActiveUser update(ActiveUser activeUser) {
        return super.update(activeUser, activeUser.getUid());
    }
}
