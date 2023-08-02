package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.PendingUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.model.PendingUser;
import com.pba.authservice.persistance.repository.PendingUserDao;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    private PendingUserMockGenerator pendingUserMockGenerator;

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

    @BeforeEach
    public void setUp() {
        pendingUserMockGenerator = new PendingUserMockGenerator();
    }

    @Test
    public void testSavePendingUser() throws IllegalAccessException {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();

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
        List<PendingUser> pendingUserList = pendingUserMockGenerator.generateMockListOfPendingUsers(10);
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
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);

        // when
        Optional<PendingUser> result = pendingUserDao.getById(pendingUser.getUid());

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
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.deleteById(pendingUser.getUid());

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
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();
        PendingUser newPendingUser = pendingUserMockGenerator.generateMockPendingUser();
        newPendingUser.setUid(pendingUser.getUid());
        pendingUserDao.save(pendingUser);

        // when
        PendingUser result = pendingUserDao.update(newPendingUser, pendingUser.getUid());

        // then
        Assertions.assertEquals(newPendingUser.getUid(), newPendingUser.getUid());
        Assertions.assertEquals(newPendingUser.getUsername(), pendingUserDao.getById(pendingUser.getUid()).get().getUsername());
    }

    @Test
    public void testUpdateEmptyPendingUser() throws IllegalAccessException {
        // given
        PendingUser pendingUser = pendingUserMockGenerator.generateMockPendingUser();

        try {
            // when
            pendingUserDao.update(pendingUser, pendingUser.getUid());
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", pendingUser.getUid().toString()), authDaoException.getMessage());
        }
    }
}