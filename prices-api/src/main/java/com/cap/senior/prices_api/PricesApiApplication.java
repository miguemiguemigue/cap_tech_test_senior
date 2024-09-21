package com.cap.senior.prices_api;

import com.cap.senior.prices_api.adapter.out.database.mongo.repository.ReactiveMongoPriceRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackageClasses = ReactiveMongoPriceRepository.class)
public class PricesApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricesApiApplication.class, args);
    }


}
