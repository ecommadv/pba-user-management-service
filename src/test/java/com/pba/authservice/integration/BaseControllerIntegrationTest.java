package com.pba.authservice.integration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:/cleanup.sql", "classpath:/data.sql"})
@AutoConfigureMockMvc
public class BaseControllerIntegrationTest {
    @ServiceConnection
    private static PostgreSQLContainer postgreSQLContainer = PostgreSqlContainerConfig.getInstance();

    static {
        postgreSQLContainer.start();
    }
}
