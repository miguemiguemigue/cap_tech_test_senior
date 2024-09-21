package com.cap.senior.prices_api.adapter.out.database.mongo.mongock.config;

import com.cap.senior.prices_api.adapter.out.database.mongo.config.MongoConfig;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import io.mongock.driver.mongodb.reactive.driver.MongoReactiveDriver;
import io.mongock.runner.springboot.MongockSpringboot;
import io.mongock.runner.springboot.base.MongockInitializingBeanRunner;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * Mongock configuration class
 */
@Configuration
public class MongockConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String mongodbHost;

    @Value("${spring.data.mongodb.port}")
    private int mongodbPort;

    @Value("${spring.data.mongodb.database}")
    private String mongodbDatabase;

    @Value("${spring.data.mongodb.username}")
    private String mongodbUsername;

    @Value("${spring.data.mongodb.password}")
    private String mongodbPassword;

    @Bean
    public MongockInitializingBeanRunner getBuilder(MongoClient reactiveMongoClient,
                                                    ApplicationContext context) {
        return MongockSpringboot.builder()
                .setDriver(MongoReactiveDriver.withDefaultLock(reactiveMongoClient, MongoConfig.PRICE_COLLECTION_NAME))
                .addMigrationScanPackage("com.cap.senior.prices_api.adapter.out.database.mongo.mongock.config")
                .setSpringContext(context)
                .setTransactionEnabled(false)
                .buildInitializingBeanRunner();
    }

    @Bean
    MongoClient mongoClient() {
        String connectionString = String.format("mongodb://%s:%s@%s:%d/%s?authSource=admin",
                mongodbUsername, mongodbPassword, mongodbHost, mongodbPort, mongodbDatabase);

        CodecRegistry codecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(codecRegistry)
                .build());
    }
}
