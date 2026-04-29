package com.intergotelecom.config;

import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(PostgresTestResource.class)
@QuarkusTestResource(RedisTestResource.class)
public abstract class BaseIntegrationTest {
}
