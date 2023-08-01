package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/cleanup.sql")
public class ActiveUserDaoIntegrationTest {
    @Autowired
    private ActiveUserDao activeUserDao;

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
    public void testSaveActiveUser() throws IllegalAccessException {
        // given
        ActiveUser activeUser = new ActiveUser(5, UUID.randomUUID(), "abc", "def");

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
    }

    private void addMockListOfActiveUsers(List<ActiveUser> activeUserList) throws IllegalAccessException {
        for (ActiveUser activeUser : activeUserList) {
            activeUserDao.save(activeUser);
        }
    }

    private List<UUID> extractUids(List<ActiveUser> activeUserList) {
        return activeUserList.stream().map((activeUser -> activeUser.getUid())).collect(Collectors.toList());
    }
    @Test
    public void testGetAllActiveUsers() throws IllegalAccessException {
        // given
        List<ActiveUser> activeUserList = List.of(
                new ActiveUser(1, UUID.randomUUID(), "abc", "def"),
                new ActiveUser(2, UUID.randomUUID(), "abc", "def"),
                new ActiveUser(3, UUID.randomUUID(), "abc", "def")
        );
        this.addMockListOfActiveUsers(activeUserList);
        List<UUID> activeUsersUids = this.extractUids(activeUserList);

        // when
        List<ActiveUser> result = activeUserDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(activeUsersUids, resultUids);
    }

    @Test
    public void testGetPresentActiveUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        ActiveUser activeUser = new ActiveUser(1, uid, "abc", "def");
        activeUserDao.save(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(uid);

        // then
        Assertions.assertEquals(activeUser.getUid(), result.get().getUid());
    }

    @Test
    public void testGetEmptyActiveUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();

        // when
        Optional<ActiveUser> result = activeUserDao.getById(uid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void testDeletePresentActiveUserByUid() throws AuthDaoException, IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        ActiveUser activeUser = new ActiveUser(1, uid, "abc", "def");
        activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(uid);

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
        Assertions.assertEquals(0, activeUserDao.getAll().size());
    }

    @Test
    public void testDeleteEmptyActiveUserByUid() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();

        try {
            // when
            activeUserDao.deleteById(uid);
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
        ActiveUser activeUser = new ActiveUser(1, uid, "abc", "def");
        ActiveUser newActiveUser = new ActiveUser(1, uid, "gg", "asaf");
        activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.update(newActiveUser, uid);

        // then
        Assertions.assertEquals(newActiveUser.getUid(), result.getUid());
        Assertions.assertEquals(newActiveUser.getUid(), activeUserDao.getById(uid).get().getUid());
    }

    @Test
    public void testUpdateEmptyActiveUser() throws IllegalAccessException {
        // given
        UUID uid = UUID.randomUUID();
        ActiveUser activeUser = new ActiveUser(1, uid, "abc", "def");

        try {
            // when
            activeUserDao.update(activeUser, uid);
            Assertions.fail();
        }
        catch(AuthDaoException authDaoException) {
            // then
            Assertions.assertEquals(String.format("Object with id %s is not stored!", uid.toString()), authDaoException.getMessage());
        }
    }

}
