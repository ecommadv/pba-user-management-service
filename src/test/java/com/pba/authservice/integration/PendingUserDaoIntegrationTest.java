package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PendingUserDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Test
    public void testSave() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
        Assertions.assertEquals(1, pendingUserDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        List<PendingUser> pendingUserList = PendingUserMockGenerator.generateMockListOfPendingUsers(10);
        this.addMockListOfPendingUsers(pendingUserList);
        List<UUID> pendingUserUids = this.extractUids(pendingUserList);

        // when
        List<PendingUser> result = pendingUserDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(pendingUserUids, resultUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser pendingUserResult = pendingUserDao.save(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUserResult.getId());

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.get().getUid());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();

        // when
        Optional<PendingUser> result = pendingUserDao.getById(absentId);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser pendingUserResult = pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUserResult.getId());

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
        Assertions.assertEquals(0, pendingUserDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> pendingUserDao.deleteById(id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", id));
    }

    @Test
    public void testUpdatePresent() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser pendingUserResult = pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.update(newPendingUser, pendingUserResult.getId());

        // then
        Assertions.assertEquals(newPendingUser.getUid(), result.getUid());
        Assertions.assertEquals(newPendingUser.getUid(), pendingUserDao.getById(pendingUserResult.getId()).get().getUid());
    }

    @Test
    public void testUpdateAbsent() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> pendingUserDao.update(pendingUser, id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", id));
    }

    private void addMockListOfPendingUsers(List<PendingUser> pendingUserList) {
        for (PendingUser pendingUser : pendingUserList) {
            pendingUserDao.save(pendingUser);
        }
    }

    private List<UUID> extractUids(List<PendingUser> pendingUserList) {
        return pendingUserList.stream().map((PendingUser::getUid)).collect(Collectors.toList());
    }
}