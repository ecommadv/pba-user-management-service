package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserDaoUnitTest {
    @InjectMocks
    private PendingUserDao pendingUserDao;

    @Mock
    private PendingUserSqlProvider pendingUserSqlProvider;

    @Mock
    private PendingUserRowMapper pendingUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private PendingUserMockGenerator pendingUserMockGenerator;

    @BeforeEach
    public void setUp() {
        pendingUserMockGenerator = new PendingUserMockGenerator();
    }

    private void setUpGetByUid(PendingUser pendingUser) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, pendingUser.getUid())).thenReturn(List.of(pendingUser));
    }

    private void setUpEmptyGetByUid(UUID uid) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, uid)).thenReturn(List.of());
    }

    @Test
    public void testSavePendingUser() {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetByUid(pendingUser);
        when(jdbcTemplate.query("select", pendingUserRowMapper, pendingUser.getUid())).thenReturn(List.of(pendingUser));

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    public void testGetPresentPendingUserByUid() {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetByUid(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser, result.get());
    }

    @Test
    public void testGetAbsentPendingUserByUid() {
        // given
        UUID absentUid = UUID.randomUUID();
        this.setUpEmptyGetByUid(absentUid);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(absentUid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllPendingUsers() {
        // given
        List<PendingUser> pendingUserList = pendingUserMockGenerator.generateMockListOfPendingUsers(10);
        when(pendingUserSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper)).thenReturn(pendingUserList);

        // when
        List<PendingUser> result = pendingUserDao.getAll();

        // then
        Assertions.assertEquals(pendingUserList, result);
    }

    @Test
    public void testDeletePresentPendingUserByUid() throws AuthDaoException {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserSqlProvider.deleteById()).thenReturn("delete");
        this.setUpGetByUid(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    public void testDeleteAbsentPendingUserByUid() {
        // given
        UUID uid = UUID.randomUUID();
        this.setUpEmptyGetByUid(uid);

        try {
            // when
            pendingUserDao.deleteById(uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }

    @Test
    public void testUpdatePresentPendingUser() throws AuthDaoException {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = pendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetByUid(pendingUser);
        when(pendingUserSqlProvider.update()).thenReturn("update");

        // when
        PendingUser result = pendingUserDao.update(newPendingUser, pendingUser.getUid());

        // then
        Assertions.assertEquals(newPendingUser, result);
    }

    @Test
    public void testUpdateAbsentPendingUser() {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser absentPendingUser = pendingUserMockGenerator.generateMockPendingUser();
        this.setUpEmptyGetByUid(uid);

        try {
            // when
            pendingUserDao.update(absentPendingUser, uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }
}