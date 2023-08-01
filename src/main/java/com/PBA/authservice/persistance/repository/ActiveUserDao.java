package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.mappers.ActiveUserMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ActiveUserDao extends JdbcRepository<ActiveUser, UUID> {
    @Autowired
    public ActiveUserDao(ActiveUserMapper rowMapper, ActiveUserSqlProvider sqlProvider, JdbcTemplate jdbcTemplate) {
        super(rowMapper, sqlProvider, jdbcTemplate);
    }

    public ActiveUser save(ActiveUser activeUser) {
        return super.save(activeUser, activeUser.getUid());
    }
}
