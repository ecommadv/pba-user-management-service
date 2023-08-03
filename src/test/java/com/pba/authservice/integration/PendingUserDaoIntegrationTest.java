package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/cleanup.sql")
public class PendingUserDaoIntegrationTest implements BaseDaoIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Test
    @Override
    public void testSave() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
    }
    @Test
    @Override
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
    @Override
    public void testGetPresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.get().getUid());
    }

    @Test
    @Override
    public void testGetAbsentById() {
        // given
        UUID uid = UUID.randomUUID();

        // when
        Optional<PendingUser> result = pendingUserDao.getById(uid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Override
    public void testDeletePresentById() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUser.getUid());

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
        Assertions.assertEquals(0, pendingUserDao.getAll().size());
    }

    @Test
    @Override
    public void testDeleteAbsentById() {
        UUID uid = UUID.randomUUID();

        assertThatThrownBy(() -> pendingUserDao.deleteById(uid))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    @Test
    @Override
    public void testUpdatePresent() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = PendingUserMockGenerator.generateMockPendingUser();
        newPendingUser.setUid(pendingUser.getUid());
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.update(newPendingUser);

        // then
        Assertions.assertEquals(newPendingUser.getUid(), newPendingUser.getUid());
        Assertions.assertEquals(newPendingUser.getUsername(), pendingUserDao.getById(pendingUser.getUid()).get().getUsername());
    }

    @Test
    @Override
    public void testUpdateAbsent() {
        // given
        PendingUser pendingUser = PendingUserMockGenerator.generateMockPendingUser();

        assertThatThrownBy(() -> pendingUserDao.update(pendingUser))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", pendingUser.getUid().toString()));
    }

    @Container
    private static PostgreSQLContainer postgreSQLContainer = PostgreSqlContainerConfig.getInstance();

    @DynamicPropertySource
    private static void overrideProps(@NotNull DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    private void addMockListOfPendingUsers(List<PendingUser> pendingUserList) {
        for (PendingUser pendingUser : pendingUserList) {
            pendingUserDao.save(pendingUser);
        }
    }

    private List<UUID> extractUids(List<PendingUser> pendingUserList) {
        return pendingUserList.stream().map((pendingUser -> pendingUser.getUid())).collect(Collectors.toList());
    }
}