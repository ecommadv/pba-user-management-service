package com.pba.authservice.integration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlContainerConfig {
    public static PostgreSQLContainer getInstance() {

        PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:15");
        postgreSQLContainer.withInitScript("schema.sql");
        return postgreSQLContainer;
    }
}
