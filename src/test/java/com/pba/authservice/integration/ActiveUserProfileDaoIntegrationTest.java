package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.ActiveUserProfile;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserProfileDao;
import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActiveUserProfileDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private ActiveUserProfileDao activeUserProfileDao;
    @Autowired
    private ActiveUserDao activeUserDao;

    @Test
    public void testSave() {
        // given
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);

        // when
        ActiveUserProfile result = activeUserProfileDao.save(activeUserProfile);

        // then
        assertEquals(1, activeUserProfileDao.getAll().size());
        assertEquals(activeUserProfile.getUserId(), activeUserProfileDao.getById(result.getId()).get().getUserId());
    }
    @Test
    public void testGetAll() {
        // given
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        List<ActiveUserProfile> activeUserProfiles = ActiveUserMockGenerator.generateMockListOfActiveUserProfiles(activeUsers, 10);
        this.addMockListOfActiveUserProfiles(activeUserProfiles);
        List<Long> expectedIds = this.extractUserIds(activeUserProfiles);

        // when
        List<ActiveUserProfile> result = activeUserProfileDao.getAll();
        List<Long> resultedIds = this.extractUserIds(result);

        // then
        assertEquals(expectedIds, resultedIds);
    }

    @Test
    public void testGetPresentById() {
        // given
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);
        ActiveUserProfile activeUserProfileResult = activeUserProfileDao.save(activeUserProfile);

        // when
        Optional<ActiveUserProfile> result = activeUserProfileDao.getById(activeUserProfileResult.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(activeUserProfile.getUserId(), result.get().getUserId());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();

        // when
        Optional<ActiveUserProfile> result = activeUserProfileDao.getById(absentId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);
        ActiveUserProfile activeUserProfileResult = activeUserProfileDao.save(activeUserProfile);

        // when
        ActiveUserProfile result = activeUserProfileDao.deleteById(activeUserProfileResult.getId());

        // then
        assertEquals(activeUserProfile.getUserId(), result.getUserId());
        assertEquals(0, activeUserProfileDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> activeUserProfileDao.deleteById(id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", id));
    }

    @Test
    public void testUpdatePresent() {
        // given
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);
        ActiveUserProfile newActiveUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);
        ActiveUserProfile savedActiveUserProfile = activeUserProfileDao.save(activeUserProfile);

        // when
        ActiveUserProfile result = activeUserProfileDao.update(newActiveUserProfile, savedActiveUserProfile.getId());

        // then
        assertEquals(result.getUserId(), newActiveUserProfile.getUserId());
        assertEquals(newActiveUserProfile.getUserId(), activeUserProfileDao.getById(savedActiveUserProfile.getId()).get().getUserId());
    }

    @Test
    public void testUpdateAbsent() {
        this.addMockListOfActiveUsers();
        List<ActiveUser> activeUsers = activeUserDao.getAll();
        ActiveUserProfile activeUserProfile = ActiveUserMockGenerator.generateMockActiveUserProfile(activeUsers);
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> activeUserProfileDao.update(activeUserProfile, id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %d is not stored!", id));
    }

    private void addMockListOfActiveUsers() {
        List<ActiveUser> activeUsers = ActiveUserMockGenerator.generateMockListOfActiveUsers(10);
        activeUsers.forEach(activeUser -> activeUserDao.save(activeUser));
    }

    private void addMockListOfActiveUserProfiles(List<ActiveUserProfile> activeUserProfiles) {
        activeUserProfiles.forEach(activeUserProfile -> activeUserProfileDao.save(activeUserProfile));
    }

    private List<Long> extractUserIds(List<ActiveUserProfile> activeUserProfiles) {
        return activeUserProfiles.stream().map(ActiveUserProfile::getUserId).collect(Collectors.toList());
    }
}
