package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.mockgenerators.GroupMockGenerator;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.repository.GroupDao;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private GroupDao groupDao;

    @Test
    public void testSave() {
        // given
        Group group = GroupMockGenerator.generateMockGroup();

        // when
        Group result = groupDao.save(group);

        // then
        assertEquals(group.getUid(), result.getUid());
        assertEquals(1, groupDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        final int sampleSize = 10;
        List<Group> groupList = GroupMockGenerator.generateMockListOfGroups(sampleSize);
        this.addMockListOfGroups(groupList);
        List<UUID> expectedGroupUids = this.extractUids(groupList);

        // when
        List<Group> result = groupDao.getAll();
        List<UUID> resultedGroupUids = this.extractUids(result);

        // then
        assertEquals(expectedGroupUids, resultedGroupUids);
    }

    @Test
    public void testGetPresentById() {
        // given
        Group group = GroupMockGenerator.generateMockGroup();
        Group savedGroup = groupDao.save(group);

        // when
        Optional<Group> result = groupDao.getById(savedGroup.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(group.getUid(), result.get().getUid());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<Group> result = groupDao.getById(id);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        Group group = GroupMockGenerator.generateMockGroup();
        Group savedGroup = groupDao.save(group);

        // when
        Group result = groupDao.deleteById(savedGroup.getId());

        // then
        assertEquals(group.getUid(), result.getUid());
        assertEquals(0, groupDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> groupDao.deleteById(id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() {
        // given
        Group group = GroupMockGenerator.generateMockGroup();
        Group newGroup = GroupMockGenerator.generateMockGroup();
        Group savedGroup = groupDao.save(group);

        // when
        Group result = groupDao.update(newGroup, savedGroup.getId());

        // then
        assertEquals(newGroup.getUid(), result.getUid());
        assertTrue(groupDao.getById(savedGroup.getId()).isPresent());
        assertEquals(newGroup.getUid(), groupDao.getById(savedGroup.getId()).get().getUid());
    }

    @Test
    public void testUpdateAbsent() {
        // given
        Group group = GroupMockGenerator.generateMockGroup();
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> groupDao.update(group, id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    private void addMockListOfGroups(List<Group> groupList) {
        groupList.forEach(group -> groupDao.save(group));
    }

    private List<UUID> extractUids(List<Group> groupList) {
        return groupList
                .stream()
                .map(Group::getUid)
                .collect(Collectors.toList());
    }
}
