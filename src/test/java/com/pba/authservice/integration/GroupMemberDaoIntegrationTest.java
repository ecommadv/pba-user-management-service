package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.mockgenerators.GroupMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.Group;
import com.pba.authservice.persistance.model.GroupMember;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.GroupDao;
import com.pba.authservice.persistance.repository.GroupMemberDao;
import com.pba.authservice.persistance.repository.UserTypeDao;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GroupMemberDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private GroupMemberDao groupMemberDao;

    @Autowired
    private GroupDao groupDao;

    @Autowired
    private ActiveUserDao userDao;

    @Autowired
    private UserTypeDao userTypeDao;

    @BeforeEach
    public void addUsers() {
        final int sampleSize = 10;
        List<ActiveUser> activeUsers = ActiveUserMockGenerator.generateMockListOfActiveUsers(sampleSize);
        activeUsers.forEach(activeUser -> userDao.save(activeUser));
    }

    @BeforeEach
    public void addUserTypes() {
        final int sampleSize = 10;
        List<UserType> userTypes = ActiveUserMockGenerator.generateMockListOfUserTypes(sampleSize);
        userTypes.forEach(userType -> userTypeDao.save(userType));
    }

    @BeforeEach
    public void addGroups() {
        final int sampleSize = 10;
        List<Group> groupList = GroupMockGenerator.generateMockListOfGroups(sampleSize);
        groupList.forEach(group -> groupDao.save(group));
    }

    @Test
    public void testSave() {
        // given
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        List<Long> expectedIds = this.extractIds(groupMember);

        // when
        GroupMember result = groupMemberDao.save(groupMember);
        List<Long> resultedIds = this.extractIds(result);

        // then
        assertEquals(expectedIds, resultedIds);
        assertEquals(1, groupMemberDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        final int sampleSize = 10;
        List<GroupMember> groupMembers = GroupMockGenerator.generateMockListOfGroupMembers(sampleSize, userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        groupMembers.forEach(groupMember -> groupMemberDao.save(groupMember));
        List<List<Long>> expectedGroupMembersIds = this.extractIds(groupMembers);

        // when
        List<GroupMember> result = groupMemberDao.getAll();
        List<List<Long>> resultedGroupMembersIds = this.extractIds(result);

        // then
        assertEquals(expectedGroupMembersIds, resultedGroupMembersIds);
    }

    @Test
    public void testGetPresentById() {
        // given
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        GroupMember savedGroupMember = groupMemberDao.save(groupMember);

        // when
        Optional<GroupMember> result = groupMemberDao.getById(savedGroupMember.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(this.extractIds(groupMember), this.extractIds(result.get()));
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<GroupMember> result = groupMemberDao.getById(id);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        GroupMember savedGroupMember = groupMemberDao.save(groupMember);

        // when
        GroupMember result = groupMemberDao.deleteById(savedGroupMember.getId());

        // then
        assertEquals(this.extractIds(groupMember), this.extractIds(result));
        assertEquals(0, groupMemberDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> groupMemberDao.deleteById(id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() {
        // given
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        GroupMember newGroupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        GroupMember savedGroupMember = groupMemberDao.save(groupMember);

        // when
        GroupMember result = groupMemberDao.update(newGroupMember, savedGroupMember.getId());

        // then
        assertEquals(this.extractIds(newGroupMember), this.extractIds(result));
        assertTrue(groupMemberDao.getById(savedGroupMember.getId()).isPresent());
        assertEquals(this.extractIds(newGroupMember), this.extractIds(groupMemberDao.getById(savedGroupMember.getId()).get()));
    }

    @Test
    public void testUpdateAbsent() {
        // given
        GroupMember groupMember = GroupMockGenerator.generateMockGroupMember(userDao.getAll(), userTypeDao.getAll(), groupDao.getAll());
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> groupMemberDao.update(groupMember, id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    private List<Long> extractIds(GroupMember groupMember) {
        return List.of(
                groupMember.getGroupId(),
                groupMember.getUserId(),
                groupMember.getUserTypeId()
        );
    }

    private List<List<Long>> extractIds(List<GroupMember> groupMembers) {
        return groupMembers
                .stream()
                .map(this::extractIds)
                .collect(Collectors.toList());
    }
}
