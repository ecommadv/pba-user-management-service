package com.pba.authservice.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlContainerConfig {
    public static PostgreSQLContainer getInstance() {
        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15")
                .withDatabaseName("authtest")
                .withUsername("postgres")
                .withPassword("larlarlar");
        postgreSQLContainer.withInitScript("schema.sql");
        return postgreSQLContainer;
    }
}
