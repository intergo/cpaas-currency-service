package com.intergotelecom.config;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.Map;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisTestResource implements QuarkusTestResourceLifecycleManager {
    private static final int REDIS_PORT = 6379;

    private static final GenericContainer<?> REDIS =
        new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(REDIS_PORT);

    @Override
    public Map<String, String> start() {
        REDIS.start();

        String redisUrl = "redis://" + REDIS.getHost() + ":" + REDIS.getMappedPort(REDIS_PORT);
        return Map.of("quarkus.redis.hosts", redisUrl);
    }

    @Override
    public void stop() {
        REDIS.stop();
    }
}
