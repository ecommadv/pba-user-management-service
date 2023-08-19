package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.model.PendingUserProfile;
import com.pba.authservice.persistance.repository.PendingUserDao;
import com.pba.authservice.persistance.repository.PendingUserProfileDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PendingUserProfileDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private PendingUserProfileDao pendingUserProfileDao;
    @Autowired
    private PendingUserDao pendingUserDao;

    @Test
    public void testSave() {
        // given
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);

        // when
        PendingUserProfile result = pendingUserProfileDao.save(pendingUserProfile);

        // then
        assertEquals(1, pendingUserProfileDao.getAll().size());
        assertEquals(pendingUserProfile.getUserId(), pendingUserProfileDao.getById(result.getId()).get().getUserId());
    }
    @Test
    public void testGetAll() {
        // given
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        List<PendingUserProfile> pendingUserProfiles = PendingUserMockGenerator.generateMockListOfPendingUserProfiles(pendingUserList, 10);
        this.addMockListOfPendingUserProfiles(pendingUserProfiles);
        List<Long> expectedIds = this.extractUserIds(pendingUserProfiles);

        // when
        List<PendingUserProfile> result = pendingUserProfileDao.getAll();
        List<Long> resultedIds = this.extractUserIds(result);

        // then
        assertEquals(expectedIds, resultedIds);
    }

    @Test
    public void testGetPresentById() {
        // given
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        PendingUserProfile pendingUserProfileResult = pendingUserProfileDao.save(pendingUserProfile);

        // when
        Optional<PendingUserProfile> result = pendingUserProfileDao.getById(pendingUserProfileResult.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(pendingUserProfile.getUserId(), result.get().getUserId());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long absentId = new Random().nextLong();

        // when
        Optional<PendingUserProfile> result = pendingUserProfileDao.getById(absentId);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        PendingUserProfile pendingUserProfileResult = pendingUserProfileDao.save(pendingUserProfile);

        // when
        PendingUserProfile result = pendingUserProfileDao.deleteById(pendingUserProfileResult.getId());

        // then
        assertEquals(pendingUserProfile.getUserId(), result.getUserId());
        assertEquals(0, pendingUserProfileDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> pendingUserProfileDao.deleteById(id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() {
        // given
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        PendingUserProfile newPendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        PendingUserProfile savedPendingUserProfile = pendingUserProfileDao.save(pendingUserProfile);

        // when
        PendingUserProfile result = pendingUserProfileDao.update(newPendingUserProfile, savedPendingUserProfile.getId());

        // then
        assertEquals(result.getUserId(), newPendingUserProfile.getUserId());
        assertEquals(newPendingUserProfile.getUserId(), pendingUserProfileDao.getById(savedPendingUserProfile.getId()).get().getUserId());
    }

    @Test
    public void testUpdateAbsent() {
        this.addMockListOfPendingUsers();
        List<PendingUser> pendingUserList = pendingUserDao.getAll();
        PendingUserProfile pendingUserProfile = PendingUserMockGenerator.generateMockPendingUserProfile(pendingUserList);
        Long id = new Random().nextLong();

        assertThatThrownBy(() -> pendingUserProfileDao.update(pendingUserProfile, id))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage("Object not found");
    }

    private void addMockListOfPendingUsers() {
        List<PendingUser> pendingUserList = PendingUserMockGenerator.generateMockListOfPendingUsers(10);
        pendingUserList.forEach(pendingUser -> pendingUserDao.save(pendingUser));
    }

    private void addMockListOfPendingUserProfiles(List<PendingUserProfile> pendingUserProfiles) {
        pendingUserProfiles.forEach(pendingUserProfile -> pendingUserProfileDao.save(pendingUserProfile));
    }

    private List<Long> extractUserIds(List<PendingUserProfile> pendingUserProfiles) {
        return pendingUserProfiles.stream().map(PendingUserProfile::getUserId).collect(Collectors.toList());
    }
}
