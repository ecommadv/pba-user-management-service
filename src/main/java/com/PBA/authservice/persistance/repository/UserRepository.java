package com.pba.authservice.persistance.repository;

import com.pba.authservice.persistance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserDaoMapper userDaoMapper;
    private final UserSqlProvider userSqlProvider;
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, UserDaoMapper userDaoMapper, UserSqlProvider userSqlProvider) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDaoMapper = userDaoMapper;
        this.userSqlProvider = userSqlProvider;
    }
    public User save(User user) {
        String sql = userSqlProvider.insertUser();
        jdbcTemplate.update(sql, user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return user;
    }

    public List<User> findAll() {
        String sql = userSqlProvider.selectAllUsers();
        return jdbcTemplate.query(sql, userDaoMapper);
    }
}
