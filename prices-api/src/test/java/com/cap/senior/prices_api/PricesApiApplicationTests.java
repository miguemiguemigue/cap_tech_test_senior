package com.cap.senior.prices_api;

import com.cap.senior.prices_api.adapter.out.database.mongo.adapter.testcontainers.config.AbstractBaseMongoIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = {MongoRepositoriesAutoConfiguration.class})
class PricesApiApplicationTests extends AbstractBaseMongoIntegrationTest {

    @Test
    void contextLoads() {
    }

}
