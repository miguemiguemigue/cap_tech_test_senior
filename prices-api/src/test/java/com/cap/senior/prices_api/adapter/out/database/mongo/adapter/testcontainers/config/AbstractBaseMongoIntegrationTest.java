package com.cap.senior.prices_api.adapter.out.database.mongo.adapter.testcontainers.config;

import com.cap.senior.prices_api.PricesApiApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Config abstract class to initialize TestContainers.
 * In this case, only one test class will exist, but it's cleaner having an abstract for this
 */
@Testcontainers
@SpringBootTest(classes = PricesApiApplication.class)
public abstract class AbstractBaseMongoIntegrationTest {

    @Container
    static GenericContainer mongoDBContainer = new GenericContainer("mongo:7.0")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "test");

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "test_db");
        registry.add("spring.data.mongodb.username", () -> "root");
        registry.add("spring.data.mongodb.password", () -> "test");
    }
}
