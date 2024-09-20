package com.cap.senior.prices_api.adapter.out.database.mongo.mongock.config;

import com.cap.senior.prices_api.adapter.out.database.mongo.config.MongoConfig;
import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.*;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ChangeUnit(id = "price-initializer", order = "1")
public class PriceInitializerChangeUnit {

    public final static int INITIAL_PRICES = 10;
    private static final Logger logger = LoggerFactory.getLogger(PriceInitializerChangeUnit.class);

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection(MongoConfig.PRICE_COLLECTION_NAME).subscribe(subscriber);
        subscriber.await();
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(MongoConfig.PRICE_COLLECTION_NAME).drop().subscribe(subscriber);
        subscriber.await();
    }

    @Execution
    public void execution(MongoDatabase mongoDatabase) {
        // Execute only if there's no data
        SubscriberSync<Long> countSubscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(MongoConfig.PRICE_COLLECTION_NAME)
                .countDocuments()
                .subscribe(countSubscriber);

        long count = countSubscriber.getFirst();
        if (count > 0) {
            logger.info("[MONGOCK] Collection already has data, skipping execution.");
            return;
        }

        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection(MongoConfig.PRICE_COLLECTION_NAME, PriceEntity.class)
                .insertMany(generatePrices())
                .subscribe(subscriber);

        InsertManyResult result = subscriber.getFirst();
        logger.info("PriceInitializerChangeUnit.execution wasAcknowledged: {}", result.wasAcknowledged());
        result.getInsertedIds()
                .forEach((key, value) -> logger.info("Inserted id[{}] : {}", key, value));
    }

    @RollbackExecution
    public void rollbackExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<DeleteResult> subscriber = new MongoSubscriberSync<>();

        mongoDatabase
                .getCollection(MongoConfig.PRICE_COLLECTION_NAME, PriceEntity.class)
                .deleteMany(new Document())
                .subscribe(subscriber);
        DeleteResult result = subscriber.getFirst();
        logger.info("PriceInitializerChangeUnit.rollbackExecution wasAcknowledged: {}", result.wasAcknowledged());
        logger.info("PriceInitializerChangeUnit.rollbackExecution deleted count: {}", result.getDeletedCount());
    }

    private static List<PriceEntity> generatePrices() {
        return List.of(
                // Dates overlap, same product and brand, different priority
                PriceEntity.builder()
                        .id("1")
                        .productId(1L)
                        .brandId(1L)
                        .priceListId(1L)
                        .startDate(LocalDateTime.parse("2018-06-14T00:00:00"))
                        .endDate(LocalDateTime.parse("2018-12-31T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(35.50))
                        .currency("EUR")
                        .build(),
                PriceEntity.builder()
                        .id("2")
                        .productId(1L)
                        .brandId(1L)
                        .priceListId(1L)
                        .startDate(LocalDateTime.parse("2018-06-14T10:00:00"))
                        .endDate(LocalDateTime.parse("2018-10-14T15:00:00"))
                        .priority(2)
                        .price(BigDecimal.valueOf(40.00))
                        .currency("EUR")
                        .build(),
                // Dates overlap, same brand and different product
                PriceEntity.builder()
                        .id("3")
                        .productId(2L)
                        .brandId(2L)
                        .priceListId(3L)
                        .startDate(LocalDateTime.parse("2019-06-14T00:00:00"))
                        .endDate(LocalDateTime.parse("2019-07-14T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(25.75))
                        .currency("EUR")
                        .build(),
                PriceEntity.builder()
                        .id("4")
                        .productId(4L)
                        .brandId(2L)
                        .priceListId(4L)
                        .startDate(LocalDateTime.parse("2019-07-01T00:00:00"))
                        .endDate(LocalDateTime.parse("2019-12-31T23:59:59"))
                        .priority(2)
                        .price(BigDecimal.valueOf(28.50))
                        .currency("EUR")
                        .build(),

                // Dates overlap, same product and different brand
                PriceEntity.builder()
                        .id("5")
                        .productId(13L)
                        .brandId(10L)
                        .priceListId(5L)
                        .startDate(LocalDateTime.parse("2020-08-01T00:00:00"))
                        .endDate(LocalDateTime.parse("2020-08-31T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(50.00))
                        .currency("EUR")
                        .build(),
                PriceEntity.builder()
                        .id("6")
                        .productId(13L)
                        .brandId(11L)
                        .priceListId(6L)
                        .startDate(LocalDateTime.parse("2020-08-15T00:00:00"))
                        .endDate(LocalDateTime.parse("2020-09-15T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(52.00))
                        .currency("EUR")
                        .build(),
                // Dates do not overlap, different product and brand
                PriceEntity.builder()
                        .id("7")
                        .productId(5L)
                        .brandId(3L)
                        .priceListId(9L)
                        .startDate(LocalDateTime.parse("2021-09-01T00:00:00"))
                        .endDate(LocalDateTime.parse("2021-09-30T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(70.00))
                        .currency("EUR")
                        .build(),
                PriceEntity.builder()
                        .id("8")
                        .productId(6L)
                        .brandId(3L)
                        .priceListId(10L)
                        .startDate(LocalDateTime.parse("2021-10-01T00:00:00"))
                        .endDate(LocalDateTime.parse("2021-12-31T23:59:59"))
                        .priority(1)
                        .price(BigDecimal.valueOf(75.00))
                        .currency("EUR")
                        .build()
        );
    }


}
