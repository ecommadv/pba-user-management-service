package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.PendingUserDaoImpl;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserDaoUnitTest implements BaseDaoUnitTest {
    @InjectMocks
    private PendingUserDaoImpl pendingUserDao;

    @Mock
    private PendingUserSqlProvider pendingUserSqlProvider;

    @Mock
    private PendingUserRowMapper pendingUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    @Override
    public void testSave() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetByUid(pendingUser);
        when(jdbcTemplate.query("select", pendingUserRowMapper, pendingUser.getUid())).thenReturn(List.of(pendingUser));

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    @Override
    public void testGetPresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetByUid(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser, result.get());
    }

    @Test
    @Override
    public void testGetAbsentById() {
        // given
        UUID absentUid = UUID.randomUUID();
        this.setUpEmptyGetByUid(absentUid);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(absentUid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Override
    public void testGetAll() {
        // given
        List<PendingUser> pendingUserList = PendingUserMockGenerator.generateMockListOfPendingUsers(10);
        when(pendingUserSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper)).thenReturn(pendingUserList);

        // when
        List<PendingUser> result = pendingUserDao.getAll();

        // then
        Assertions.assertEquals(pendingUserList, result);
    }

    @Test
    @Override
    public void testDeletePresentById() throws AuthDaoException {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserSqlProvider.deleteById()).thenReturn("delete");
        this.setUpGetByUid(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    @Override
    public void testDeleteAbsentById() {
        // given
        UUID uid = UUID.randomUUID();
        this.setUpEmptyGetByUid(uid);

        assertThatThrownBy(() -> pendingUserDao.deleteById(uid))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    @Test
    @Override
    public void testUpdatePresent() throws AuthDaoException {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        newPendingUser.setUid(pendingUser.getUid());
        this.setUpGetByUid(pendingUser);
        when(pendingUserSqlProvider.update()).thenReturn("update");

        // when
        PendingUser result = pendingUserDao.update(newPendingUser);

        // then
        Assertions.assertEquals(newPendingUser, result);
    }

    @Test
    @Override
    public void testUpdateAbsent() {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser absentPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        absentPendingUser.setUid(uid);
        this.setUpEmptyGetByUid(uid);

        assertThatThrownBy(() -> pendingUserDao.update(absentPendingUser))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    private void setUpGetByUid(PendingUser pendingUser) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, pendingUser.getUid())).thenReturn(List.of(pendingUser));
    }

    private void setUpEmptyGetByUid(UUID uid) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, uid)).thenReturn(List.of());
    }
}