package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoNotFoundException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.UserType;
import com.pba.authservice.persistance.repository.UserTypeDao;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTypeDaoIntegrationTest extends BaseDaoIntegrationTest {
    @Autowired
    private UserTypeDao userTypeDao;

    @Test
    public void testSave() {
        // given
        UserType userType = ActiveUserMockGenerator.generateMockUserType();

        // when
        UserType result = userTypeDao.save(userType);

        // then
        assertEquals(userType.getName(), result.getName());
        assertEquals(1, userTypeDao.getAll().size());
    }
    @Test
    public void testGetAll() {
        // given
        final int sampleSize = 10;
        List<UserType> userTypes = ActiveUserMockGenerator.generateMockListOfUserTypes(sampleSize);
        this.addMockListOfUserTypes(userTypes);
        List<String> expectedUserTypeNames = this.extractNames(userTypes);

        // when
        List<UserType> result = userTypeDao.getAll();
        List<String> resultedUserTypeNames = this.extractNames(result);

        // then
        assertEquals(expectedUserTypeNames, resultedUserTypeNames);
    }

    @Test
    public void testGetPresentById() {
        // given
        UserType userType = ActiveUserMockGenerator.generateMockUserType();
        UserType savedUserType = userTypeDao.save(userType);

        // when
        Optional<UserType> result = userTypeDao.getById(savedUserType.getId());

        // then
        assertTrue(result.isPresent());
        assertEquals(userType.getName(), result.get().getName());
    }

    @Test
    public void testGetAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        Optional<UserType> result = userTypeDao.getById(id);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentById() {
        // given
        UserType userType = ActiveUserMockGenerator.generateMockUserType();
        UserType savedUserType = userTypeDao.save(userType);

        // when
        UserType result = userTypeDao.deleteById(savedUserType.getId());

        // then
        assertEquals(userType.getName(), result.getName());
        assertEquals(0, userTypeDao.getAll().size());
    }

    @Test
    public void testDeleteAbsentById() {
        // given
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> userTypeDao.deleteById(id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    @Test
    public void testUpdatePresent() {
        // given
        UserType userType = ActiveUserMockGenerator.generateMockUserType();
        UserType newUserType = ActiveUserMockGenerator.generateMockUserType();
        UserType savedUserType = userTypeDao.save(userType);

        // when
        UserType result = userTypeDao.update(newUserType, savedUserType.getId());

        // then
        assertEquals(newUserType.getName(), result.getName());
        assertTrue(userTypeDao.getById(savedUserType.getId()).isPresent());
        assertEquals(newUserType.getName(), userTypeDao.getById(savedUserType.getId()).get().getName());
    }

    @Test
    public void testUpdateAbsent() {
        // given
        UserType userType = ActiveUserMockGenerator.generateMockUserType();
        Long id = new Random().nextLong();

        // when
        ThrowableAssert.ThrowingCallable supplier = () -> userTypeDao.update(userType, id);

        // then
        assertThatThrownBy(supplier)
                .isInstanceOf(AuthDaoNotFoundException.class)
                .hasMessage("Object not found");
    }

    private void addMockListOfUserTypes(List<UserType> userTypes) {
        userTypes.forEach(userType -> userTypeDao.save(userType));
    }

    private List<String> extractNames(List<UserType> userTypes) {
        return userTypes
                .stream()
                .map(UserType::getName)
                .collect(Collectors.toList());
    }
}
