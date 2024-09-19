package com.cap.senior.prices_api.adapter.out.database.mongo.adapter;

import com.cap.senior.prices_api.adapter.out.database.mongo.adapter.testcontainers.config.AbstractBaseMongoIntegrationTest;
import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import com.cap.senior.prices_api.adapter.out.database.mongo.repository.ReactiveMongoPriceRepository;
import com.cap.senior.prices_api.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;


class PriceRepositoryAdapterTest extends AbstractBaseMongoIntegrationTest {

    @Autowired
    private ReactiveMongoPriceRepository reactiveMongoPriceRepository;

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @BeforeEach
    void setUp() {
        // Delete collection before each test
        reactiveMongoPriceRepository.deleteAll().block();
    }

    /**
     * This test creates several prices that match by valid date, but only one that match by date and ids.
     * Should find the only one that matches all conditions.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_only_one_matching_date_product_and_brand_id_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);
        reactiveMongoPriceRepository.saveAll(Flux.just(
                // should find this one
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(testPrice)
                        .priority(1)
                        .build(),
                // valid date, but different product and brand ids
                PriceEntity.builder()
                        .id("2")
                        .productId(testProductId + 1L)
                        .brandId(testBrandId + 1L)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                PriceEntity.builder()
                        .id("3")
                        .productId(testProductId + 2L)
                        .brandId(testBrandId + 2L)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(39.99))
                        .priority(1)
                        .build()
        )).blockLast();

        LocalDateTime testDate = of.plusDays(1);

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectNextMatches(
                        price -> price.getPrice().equals(testPrice) &&
                                price.getBrandId().equals(testBrandId) &&
                                price.getProductId().equals(testProductId)
                )
                .verifyComplete();
    }

    /**
     * This test creates several prices that match by product id and valid date, but only one that match by date and ids.
     * Should find the only one that matches all conditions.
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_product_but_only_one_match_all_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);
        reactiveMongoPriceRepository.saveAll(Flux.just(
                // should find this one
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(testPrice)
                        .priority(1)
                        .build(),
                // same product and valid dates, but different brand id
                PriceEntity.builder()
                        .id("2")
                        .productId(testProductId)
                        .brandId(testBrandId + 1L)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                PriceEntity.builder()
                        .id("3")
                        .productId(testProductId)
                        .brandId(testBrandId + 2L)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(39.99))
                        .priority(1)
                        .build()
        )).blockLast();

        LocalDateTime testDate = of.plusDays(1);

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectNextMatches(
                        price -> price.getPrice().equals(testPrice) &&
                                price.getBrandId().equals(testBrandId) &&
                                price.getProductId().equals(testProductId)
                )
                .verifyComplete();
    }

    /**
     * This test creates several prices that match by brand id and date, but only one that match by all three conditions.
     * Should find the only one that matches all conditions
     */
    @Test
    void getPricesByDateProductAndBrand_Given_several_prices_matching_by_date_and_brand_but_only_one_match_all_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime of = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);
        reactiveMongoPriceRepository.saveAll(Flux.just(
                // same brand and valid dates, but different product id
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId + 1L)
                        .brandId(testBrandId)
                        .startDate(of)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(19.99))
                        .priority(1)
                        .build(),
                PriceEntity.builder()
                        .id("2")
                        .productId(testProductId + 2L)
                        .brandId(testBrandId)
                        .startDate(LocalDateTime.of(2024, 9, 19, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(19.99))
                        .priority(2)
                        .build(),
                // should find this one
                PriceEntity.builder()
                        .id("3")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(LocalDateTime.of(2024, 9, 17, 0, 0))
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(testPrice)
                        .priority(1)
                        .build()
        )).blockLast();

        LocalDateTime testDate = of.plusDays(1);

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectNextMatches(
                        price -> price.getPrice().equals(testPrice) &&
                                price.getBrandId().equals(testBrandId) &&
                                price.getProductId().equals(testProductId)
                )
                .verifyComplete();
    }

    /**
     * This test creates one price which start date is the same of the search date
     * Should find it
     */
    @Test
    void getPricesByDateProductAndBrand_Given_start_date_equals_search_date_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime testDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(29.99);
        reactiveMongoPriceRepository.save(
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(testDate)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(testPrice)
                        .priority(1)
                        .build()
        ).block();

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectNextMatches(
                        price -> price.getPrice().equals(testPrice) &&
                                price.getBrandId().equals(testBrandId) &&
                                price.getProductId().equals(testProductId)
                )
                .verifyComplete();
    }

    /**
     * This test creates one price which start date greater than the search date
     * Should not find it
     */
    @Test
    void getPricesByDateProductAndBrand_Given_start_date_greater_than_search_date_Should_not_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        reactiveMongoPriceRepository.save(
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(LocalDateTime.of(2024, 9, 22, 0, 0))
                        .price(BigDecimal.valueOf(19.99))
                        .priority(1)
                        .build()
        ).block();

        LocalDateTime testDate = startDate.minusSeconds(1);

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    /**
     * This test creates one price which end date is equal to the search date
     * Should find it
     * Note: price which end date is equal to the search date should not be discarded, that's application logic that
     * shouldn't go here
     */
    @Test
    void getPricesByDateProductAndBrand_Given_end_date_equal_than_search_date_Should_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(19.99);
        LocalDateTime endDateTest = LocalDateTime.of(2024, 9, 22, 0, 0);
        reactiveMongoPriceRepository.save(
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(endDateTest)
                        .price(testPrice)
                        .priority(1)
                        .build()
        ).block();

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(endDateTest, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectNextMatches(
                        price -> price.getPrice().equals(testPrice) &&
                                price.getBrandId().equals(testBrandId) &&
                                price.getProductId().equals(testProductId)
                )
                .verifyComplete();
    }

    /**
     * This test creates one price which end date is less than search date
     * Should not find it
     */
    @Test
    void getPricesByDateProductAndBrand_Given_end_date_less_than_search_date_Should_not_find_it() {
        // Given:
        long testProductId = 123L;
        long testBrandId = 456L;
        LocalDateTime startDate = LocalDateTime.of(2024, 9, 20, 0, 0);
        BigDecimal testPrice = BigDecimal.valueOf(19.99);
        LocalDateTime endDateTest = LocalDateTime.of(2024, 9, 22, 0, 0);
        reactiveMongoPriceRepository.save(
                PriceEntity.builder()
                        .id("1")
                        .productId(testProductId)
                        .brandId(testBrandId)
                        .startDate(startDate)
                        .endDate(endDateTest)
                        .price(testPrice)
                        .priority(1)
                        .build()
        ).block();

        LocalDateTime testDate = endDateTest.plusSeconds(1);

        // When:
        Flux<Price> result = priceRepositoryAdapter.getPricesByDateProductAndBrand(testDate, testProductId, testBrandId);

        // Then:
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

}