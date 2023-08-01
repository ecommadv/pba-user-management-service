package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.persistance.model.ActiveUser;
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

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/cleanup.sql")
public class PendingUserDaoIntegrationTest {
    @Autowired
    private PendingUserDao pendingUserDao;

    @Container
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("larlarlar");

    static {
        postgreSQLContainer.withInitScript("schema.sql");
    }

    @DynamicPropertySource
    public static void overrideProps(@NotNull DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    public void testSavePendingUser() throws IllegalAccessException {
        // given
        PendingUser pendingUser = new PendingUser(1, UUID.randomUUID(), "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID());

        // when
        PendingUser result = pendingUserDao.save(pendingUser);

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
    }

    private void addMockListOfPendingUsers(List<PendingUser> pendingUserList) throws IllegalAccessException {
        for (PendingUser pendingUser : pendingUserList) {
            pendingUserDao.save(pendingUser);
        }
    }

    private List<UUID> extractUids(List<PendingUser> pendingUserList) {
        return pendingUserList.stream().map((pendingUser -> pendingUser.getUid())).collect(Collectors.toList());
    }
    @Test
    public void testGetAllPendingUsers() throws IllegalAccessException {
        // given
        List<PendingUser> pendingUserList = List.of(
                new PendingUser(1, UUID.randomUUID(), "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID()),
                new PendingUser(1, UUID.randomUUID(), "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID()),
                new PendingUser(1, UUID.randomUUID(), "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID())
        );
        this.addMockListOfPendingUsers(pendingUserList);
        List<UUID> pendingUserUids = this.extractUids(pendingUserList);

        // when
        List<PendingUser> result = pendingUserDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(pendingUserUids, resultUids);
    }

    @Test
    public void testGetPresentPendingUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser pendingUser = new PendingUser(1, uid, "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID());
        pendingUserDao.save(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(uid);

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.get().getUid());
    }

    @Test
    public void testGetEmptyPendingUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();

        // when
        Optional<PendingUser> result = pendingUserDao.getById(uid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentPendingUserByUid() throws AuthDaoException, IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser pendingUser = new PendingUser(1, uid, "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID());
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(uid);

        // then
        Assertions.assertEquals(pendingUser.getUid(), result.getUid());
        Assertions.assertEquals(0, pendingUserDao.getAll().size());
    }

    @Test
    public void testDeleteEmptyPendingUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();

        try {
            // when
            pendingUserDao.deleteById(uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }

    @Test
    public void testUpdatePresentActiveUser() throws IllegalAccessException, AuthDaoException {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser pendingUser = new PendingUser(1, uid, "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID());
        PendingUser newPendingUser = new PendingUser(1, uid, "new", "def", "ghi", new Timestamp(0), UUID.randomUUID());
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.update(newPendingUser, uid);

        // then
        Assertions.assertEquals(newPendingUser.getUid(), newPendingUser.getUid());
        Assertions.assertEquals(newPendingUser.getUsername(), pendingUserDao.getById(uid).get().getUsername());
    }

    @Test
    public void testUpdateEmptyPendingUser() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        PendingUser pendingUser = new PendingUser(1, uid, "abc", "def", "ghi", new Timestamp(0), UUID.randomUUID());

        try {
            // when
            pendingUserDao.update(pendingUser, uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }
}