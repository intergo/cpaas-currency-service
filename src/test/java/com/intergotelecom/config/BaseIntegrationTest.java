package com.intergotelecom.config;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.mongodb.MongoTestResource;

@QuarkusTestResource(MongoTestResource.class)
public abstract class BaseIntegrationTest {
}
