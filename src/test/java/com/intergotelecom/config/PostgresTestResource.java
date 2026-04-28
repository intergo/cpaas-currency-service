package com.intergotelecom.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestResource implements QuarkusTestResourceLifecycleManager {
    private static final PostgreSQLContainer<?> POSTGRES =
        new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("cpaas_currency_test")
            .withUsername("test")
            .withPassword("test");

    @Override
    public Map<String, String> start() {
        POSTGRES.start();

        return Map.of(
            "quarkus.datasource.jdbc.url", POSTGRES.getJdbcUrl(),
            "quarkus.datasource.username", POSTGRES.getUsername(),
            "quarkus.datasource.password", POSTGRES.getPassword()
        );
    }

    @Override
    public void stop() {
        POSTGRES.stop();
    }
}
