package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserDaoUnitTest {
    @InjectMocks
    private ActiveUserDao activeUserDao;

    @Mock
    private ActiveUserSqlProvider activeUserSqlProvider;

    @Mock
    private ActiveUserRowMapper activeUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private ActiveUserMockGenerator activeUserMockGenerator;

    @BeforeEach
    public void setUp() {
        activeUserMockGenerator = new ActiveUserMockGenerator();
    }

    private void setUpGetByUid(ActiveUser activeUser) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, activeUser.getUid())).thenReturn(List.of(activeUser));
    }

    private void setUpEmptyGetByUid(UUID uid) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, uid)).thenReturn(List.of());
    }

    @Test
    public void testSaveActiveUser() {
        // given
        ActiveUser activeUser = activeUserMockGenerator.generateMockActiveUser();
        this.setUpGetByUid(activeUser);
        when(jdbcTemplate.query("select", activeUserRowMapper, activeUser.getUid())).thenReturn(List.of(activeUser));

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    public void testGetPresentActiveUserByUid() {
        // given
        ActiveUser activeUser = activeUserMockGenerator.generateMockActiveUser();
        this.setUpGetByUid(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser, result.get());
    }

    @Test
    public void testGetAbsentActiveUserByUid() {
        // given
        UUID absentUid = UUID.randomUUID();
        this.setUpEmptyGetByUid(absentUid);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(absentUid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllActiveUsers() {
        // given
        List<ActiveUser> activeUserList = activeUserMockGenerator.generateMockListOfActiveUsers(10);
        when(activeUserSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper)).thenReturn(activeUserList);

        // when
        List<ActiveUser> result = activeUserDao.getAll();

        // then
        Assertions.assertEquals(activeUserList, result);
    }

    @Test
    public void testDeletePresentActiveUserByUid() throws AuthDaoException {
        // given
        ActiveUser activeUser = activeUserMockGenerator.generateMockActiveUser();
        when(activeUserSqlProvider.deleteById()).thenReturn("delete");
        this.setUpGetByUid(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    public void testDeleteAbsentActiveUserByUid() {
        // given
        UUID uid = UUID.randomUUID();
        this.setUpEmptyGetByUid(uid);

        try {
            // when
            activeUserDao.deleteById(uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }

    @Test
    public void testUpdatePresentActiveUser() throws AuthDaoException {
        // given
        ActiveUser activeUser = activeUserMockGenerator.generateMockActiveUser();
        ActiveUser newActiveUser = activeUserMockGenerator.generateMockActiveUser();
        this.setUpGetByUid(activeUser);
        when(activeUserSqlProvider.update()).thenReturn("update");

        // when
        ActiveUser result = activeUserDao.update(newActiveUser, activeUser.getUid());

        // then
        Assertions.assertEquals(newActiveUser, result);
    }

    @Test
    public void testUpdateAbsentActiveUser() {
        // given
        UUID uid = UUID.randomUUID();
        ActiveUser absentActiveUser = activeUserMockGenerator.generateMockActiveUser();
        this.setUpEmptyGetByUid(uid);

        try {
            // when
            activeUserDao.update(absentActiveUser, uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }
}
