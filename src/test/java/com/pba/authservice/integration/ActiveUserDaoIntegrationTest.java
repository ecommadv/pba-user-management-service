package com.pba.authservice.integration;

import com.pba.authservice.exceptions.AuthDaoException;
import com.pba.authservice.mockgenerators.ActiveUserMockGenerator;
import com.pba.authservice.persistance.model.ActiveUser;
import com.pba.authservice.persistance.repository.ActiveUserDao;
import com.pba.authservice.persistance.repository.ActiveUserDaoImpl;
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
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/cleanup.sql")
public class ActiveUserDaoIntegrationTest implements BaseDaoIntegrationTest {
    @Autowired
    private ActiveUserDao activeUserDao;

    @DynamicPropertySource
    public static void overrideProps(@NotNull DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    @Override
    public void testSave() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();

        // when
        ActiveUser result = activeUserDao.save(activeUser);

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
    }
    @Test
    @Override
    public void testGetAll() {
        // given
        List<ActiveUser> activeUserList = ActiveUserMockGenerator.generateMockListOfActiveUsers(10);
        this.addMockListOfActiveUsers(activeUserList);
        List<UUID> activeUsersUids = this.extractUids(activeUserList);

        // when
        List<ActiveUser> result = activeUserDao.getAll();
        List<UUID> resultUids = this.extractUids(result);

        // then
        Assertions.assertEquals(activeUsersUids, resultUids);
    }

    @Test
    @Override
    public void testGetPresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        activeUserDao.save(activeUser);

        // when
        Optional<ActiveUser> result = activeUserDao.getById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser.getUid(), result.get().getUid());
    }

    @Test
    @Override
    public void testGetAbsentById() {
        // given
        UUID uid = UUID.randomUUID();

        // when
        Optional<ActiveUser> result = activeUserDao.getById(uid);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @Override
    public void testDeletePresentById() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.deleteById(activeUser.getUid());

        // then
        Assertions.assertEquals(activeUser.getUid(), result.getUid());
        Assertions.assertEquals(0, activeUserDao.getAll().size());
    }

    @Test
    @Override
    public void testDeleteAbsentById() {
        UUID uid = UUID.randomUUID();

        assertThatThrownBy(() -> activeUserDao.deleteById(uid))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", uid.toString()));
    }

    @Test
    @Override
    public void testUpdatePresent() {
        // given
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();
        ActiveUser newActiveUser = ActiveUserMockGenerator.generateMockActiveUser();
        newActiveUser.setUid(activeUser.getUid());
        activeUserDao.save(activeUser);

        // when
        ActiveUser result = activeUserDao.update(newActiveUser);

        // then
        Assertions.assertEquals(newActiveUser.getUid(), result.getUid());
        Assertions.assertEquals(newActiveUser.getUid(), activeUserDao.getById(activeUser.getUid()).get().getUid());
    }

    @Test
    @Override
    public void testUpdateAbsent() {
        ActiveUser activeUser = ActiveUserMockGenerator.generateMockActiveUser();

        assertThatThrownBy(() -> activeUserDao.update(activeUser))
                .isInstanceOf(AuthDaoException.class)
                .hasMessage(String.format("Object with id %s is not stored!", activeUser.getUid().toString()));
    }

    @Container
    private static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("postgres")
            .withPassword("larlarlar");

    static {
        postgreSQLContainer.withInitScript("schema.sql");
    }

    private void addMockListOfActiveUsers(List<ActiveUser> activeUserList) {
        for (ActiveUser activeUser : activeUserList) {
            activeUserDao.save(activeUser);
        }
    }

    private List<UUID> extractUids(List<ActiveUser> activeUserList) {
        return activeUserList.stream().map((activeUser -> activeUser.getUid())).collect(Collectors.toList());
    }

}
