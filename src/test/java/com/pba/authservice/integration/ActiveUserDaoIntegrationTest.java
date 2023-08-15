package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

public class ActiveUserDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private ActiveUserDao activeUserDao;

    @Test
    public void testSave() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
        Assertions.assertEquals(1, activeUserDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<ActiveUser> activeUserList = ActiveUserMockGenerator.generateMockListOfActiveUsers(10);
        this.addMockListOfActiveUsers(activeUserList);
        List<UUID> activeUsersUids = this.extractUids(activeUserList);

        // when
        List<ActiveUser> result = activeUserDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(activeUsersUids, resultUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser activeUserResult = activeUserDao.save(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(activeUserResult.getId());

        // then
        Assertions.assertEquals(activeUser.getUid(), result.get().getUid());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<ActiveUser> result = activeUserDao.getById(id);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser activeUserResult = activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(activeUserResult.getId());

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
        Assertions.assertEquals(0, activeUserDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> activeUserDao.deleteById(id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    @Test
    public void testUpdatePresent() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser newActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser activeUserResult = activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.update(newActiveUser, activeUserResult.getId());

        // then
        Assertions.assertEquals(newActiveUser.getUid(), result.getUid());
        Assertions.assertEquals(newActiveUser.getUid(), activeUserDao.getById(activeUserResult.getId()).get().getUid());
    }

    @Test
    public void testUpdateAbsent() {
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> activeUserDao.update(activeUser, id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", id.toString()));
    }

    private void addMockListOfActiveUsers(List<ActiveUser> activeUserList) {
        for (ActiveUser activeUser : activeUserList) {
            activeUserDao.save(activeUser);
        }
    }

    private List<UUID> extractUids(List<ActiveUser> activeUserList) {
        return activeUserList.stream().map((ActiveUser::getUid)).collect(Collectors.toList());
    }

}
