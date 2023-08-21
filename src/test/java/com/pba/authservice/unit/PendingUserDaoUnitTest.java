package com.pba.authservice.unit;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDaoImpl;
import com.pba.authservice.persistance.repository.UtilsFactory;
import com.pba.authservice.persistance.repository.mappers.PendingUserRowMapper;
import com.pba.authservice.persistance.repository.sql.PendingUserSqlProvider;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PendingUserDaoUnitTest {
    @InjectMocks
    private PendingUserDaoImpl pendingUserDao;

    @Mock
    private PendingUserSqlProvider pendingUserSqlProvider;

    @Mock
    private PendingUserRowMapper pendingUserRowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private UtilsFactory utilsFactory;

    @Mock
    private KeyHolder keyHolder;

    @Test
    public void testSave() throws Exception {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        pendingUser.setId(1L);
        this.setUpGetById(pendingUser);

        Map<String, Object> keys = new HashMap<>();
        keys.put("id", 1L);
        when(keyHolder.getKeys()).thenReturn(keys);
        when(utilsFactory.keyHolder()).thenReturn(keyHolder);

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    public void testGetPresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        this.setUpGetById(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUser.getId());

        // then
        Assertions.assertEquals(pendingUser, result.get());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
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
    public void testDeletePresentById() throws AuthDaoException {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        when(pendingUserSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", pendingUser.getId())).thenReturn(1);
        this.setUpGetById(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUser.getId());

        // then
        Assertions.assertEquals(pendingUser, result);
    }

    @Test
    public void testDeleteAbsentById() {
        Long absentId = new Random().nextLong();
        this.setUpEmptyGetById(absentId);
        when(pendingUserSqlProvider.deleteById()).thenReturn("delete");
        when(jdbcTemplate.update("delete", absentId)).thenReturn(0);

        assertThatThrownBy(() -> pendingUserDao.deleteById(absentId))
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() throws AuthDaoException {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        newPendingUser.setId(pendingUser.getId());
        when(pendingUserSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                newPendingUser.getUid(),
                newPendingUser.getUsername(),
                newPendingUser.getPassword(),
                newPendingUser.getCreatedAt(),
                newPendingUser.getValidationCode(),
                newPendingUser.getId())).thenReturn(1);

        // when
        PendingUser result = pendingUserDao.update(newPendingUser, newPendingUser.getId());

        // then
        Assertions.assertEquals(newPendingUser, result);
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Long id = new Random().nextLong();
        PendingUser absentPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        absentPendingUser.setId(id);
        when(pendingUserSqlProvider.update()).thenReturn("update");
        when(jdbcTemplate.update("update",
                absentPendingUser.getUid(),
                absentPendingUser.getUsername(),
                absentPendingUser.getPassword(),
                absentPendingUser.getCreatedAt(),
                absentPendingUser.getValidationCode(),
                id)).thenReturn(0);

        assertThatThrownBy(() -> pendingUserDao.update(absentPendingUser, id))
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    private void setUpGetById(PendingUser pendingUser) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, pendingUser.getId())).thenReturn(List.of(pendingUser));
    }

    private void setUpEmptyGetById(Long id) {
        when(pendingUserSqlProvider.selectById()).thenReturn("select");
        when(jdbcTemplate.query("select", pendingUserRowMapper, id)).thenReturn(List.of());
    }
}
