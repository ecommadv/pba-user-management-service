package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
import com.pba.authservice.persistance.repository.UtilsFactory;
import com.pba.authservice.persistance.repository.mappers.ActiveUserRowMapper;
import com.pba.authservice.persistance.repository.sql.ActiveUserSqlProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActiveUserDaoUnitTest {
    @InjectMocks
    private ActiveUserDaoImpl activeUserDao;

    @Mock
    private ActiveUserSqlProvider activeUserSqlProvider;

    @Mock
    private ActiveUserRowMapper activeUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        activeUser.setId(1L);
        this.setUpGetById(activeUser);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        this.setUpGetById(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(activeUser.getId());

        // then
        Assertions.assertEquals(activeUser, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
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
    public void testDeletePresentById() throws AuthDaoException {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        when(activeUserSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", activeUser.getId())).thenReturn(1);
        this.setUpGetById(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(activeUser.getId());

        // then
        Assertions.assertEquals(activeUser, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(activeUserSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> activeUserDao.deleteById(absentId))
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() throws AuthDaoException {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser newActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        newActiveUser.setId(activeUser.getId());
        when(activeUserSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", newActiveUser.getUid(), newActiveUser.getUsername(), newActiveUser.getPassword(), newActiveUser.getId())).thenReturn(1);

        // when
        ActiveUser result = activeUserDao.update(newActiveUser, newActiveUser.getId());

        // then
        Assertions.assertEquals(newActiveUser, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        ActiveUser absentActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        absentActiveUser.setId(id);
        when(activeUserSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update", absentActiveUser.getUid(), absentActiveUser.getUsername(), absentActiveUser.getPassword(), id)).thenReturn(0);

        assertThatThrownBy(() -> activeUserDao.update(absentActiveUser, id))
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    private void setUpGetById(ActiveUser activeUser) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, activeUser.getId())).thenReturn(List.of(activeUser));
    }

    private void setUpEmptyGetById(Long id) {
        when(activeUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", activeUserRowMapper, id)).thenReturn(List.of());
    }
}
