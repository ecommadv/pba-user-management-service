package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActiveUserDaoUnitTest implements BaseDaoUnitTest {
    @InjectMocks
    private ActiveUserDaoImpl activeUserDao;

    @Mock
    private ActiveUserSqlProvider activeUserSqlProvider;

    @Mock
    private ActiveUserRowMapper activeUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    @Override
    public void testSave() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        this.setUpGetByUid(activeUser);
        when(jdbcTemplate.query("select", activeUserRowMapper, activeUser.getUid())).thenReturn(List.of(activeUser));

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    @Override
    public void testGetPresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        this.setUpGetByUid(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser, result.get());
    }

    @Test
    @Override
    public void testGetAbsentById() {
        // given
        UUID absentUid = UUID.randomUUID();
        this.setUpEmptyGetByUid(absentUid);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(absentUid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Override
    public void testGetAll() {
        // given
        List<ActiveUser> activeUserList = ActiveUserMockGenerator.generateMockListOfActiveUsers(10);
        when(activeUserSqlProvider.selectAll()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper)).thenReturn(activeUserList);

        // when
        List<ActiveUser> result = activeUserDao.getAll();

        // then
        Assertions.assertEquals(activeUserList, result);
    }

    @Test
    @Override
    public void testDeletePresentById() throws AuthDaoException {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        when(activeUserSqlProvider.deleteById()).thenReturn("delete");
        this.setUpGetByUid(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    @Override
    public void testDeleteAbsentById() {
        UUID uid = UUID.randomUUID();
        this.setUpEmptyGetByUid(uid);

        assertThatThrownBy(() -> activeUserDao.deleteById(uid))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    @Test
    @Override
    public void testUpdatePresent() throws AuthDaoException {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser newActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        newActiveUser.setUid(activeUser.getUid());
        this.setUpGetByUid(activeUser);
        when(activeUserSqlProvider.update()).thenReturn("update");

        // when
        ActiveUser result = activeUserDao.update(newActiveUser);

        // then
        Assertions.assertEquals(newActiveUser, result);
    }

    @Test
    @Override
    public void testUpdateAbsent() {
        // given
        UUID uid = UUID.randomUUID();
        ActiveUser absentActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        absentActiveUser.setUid(uid);
        this.setUpEmptyGetByUid(uid);

        assertThatThrownBy(() -> activeUserDao.update(absentActiveUser))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    private void setUpGetByUid(ActiveUser activeUser) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, activeUser.getUid())).thenReturn(List.of(activeUser));
    }

    private void setUpEmptyGetByUid(UUID uid) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, uid)).thenReturn(List.of());
    }
}
