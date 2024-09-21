package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.adapter.out.database.mongo.adapter.testcontainers.config.AbstractBaseMongoIntegrationTest;
import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import com.cap.senior.prices_api.adapter.out.database.mongo.repository.ReactiveMongoPriceRepository;
import com.cap.senior.prices_api.application.services.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PricesApiIntegrationTest extends AbstractBaseMongoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @Autowired
    private PriceService priceService;

    @Autowired
    private ReactiveMongoPriceRepository priceRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        // Clean collection between tests
        priceRepository.deleteAll().block();
    }

    @Test
    void getPrices_Given_no_data_Then_return_none() {
        // Given:
        LocalDateTime date = LocalDateTime.parse("2018-06-14T12:00:00");
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date.toString())
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()

                // Then:
                .expectStatus().isNotFound();
    }


    @Test
    void getPrices_Given_two_prices_dates_overlap_Then_return_top_priority_one() {
        // Given:
        LocalDateTime date = LocalDateTime.parse("2018-06-14T12:00:00");
        Long productId = 1L;
        Long brandId = 1L;

        // Prices overlapping, price2 has top priority
        PriceEntity price1 = PriceEntity.builder()
                .id("1")
                .productId(1L)
                .brandId(1L)
                .priceListId(1L)
                .startDate(LocalDateTime.parse("2018-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2018-12-31T23:59:59"))
                .priority(1)
                .price(BigDecimal.valueOf(35.50))
                .currency("EUR")
                .build();

        LocalDateTime price2StartDate = LocalDateTime.parse("2018-06-14T10:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2018-10-14T15:00:00");
        BigDecimal price2Amount = BigDecimal.valueOf(40.00);
        String price2Currency = "EUR";
        PriceEntity price2 = PriceEntity.builder()
                .id("2")
                .productId(1L)
                .brandId(1L)
                .priceListId(1L)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .price(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1).block();
        priceRepository.save(price2).block();

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date.toString())
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()

                // Then:
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand_id").isEqualTo(brandId.intValue())
                .jsonPath("$.start_date").isEqualTo(price2StartDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.end_date").isEqualTo(price2EndDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.price_list").isEqualTo(1)
                .jsonPath("$.product_id").isEqualTo(productId.intValue())
                .jsonPath("$.price").isEqualTo(price2Amount)
                .jsonPath("$.curr").isEqualTo(price2Currency)
                .jsonPath("$.priority").doesNotExist();
    }

    @Test
    void getPrices_Given_two_prices_dates_overlap_same_brand_different_product_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long brandId = 2L;
        Long price2ProductId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates overlap, same brand and different product
        PriceEntity price1 = PriceEntity.builder()
                .id("1")
                .productId(2L)
                .brandId(brandId)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2019-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2019-07-14T23:59:59"))
                .priority(1)
                .price(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();
        PriceEntity price2 = PriceEntity.builder()
                .id("2")
                .productId(price2ProductId)
                .brandId(brandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .price(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1).block();
        priceRepository.save(price2).block();

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", price2StartDate.plusMinutes(5).toString())
                        .queryParam("product_id", price2ProductId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()

                // Then:
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand_id").isEqualTo(brandId.intValue())
                .jsonPath("$.start_date").isEqualTo(price2StartDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.end_date").isEqualTo(price2EndDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.price_list").isEqualTo(price2PriceListId)
                .jsonPath("$.product_id").isEqualTo(price2ProductId.intValue())
                .jsonPath("$.price").isEqualTo(price2Amount)
                .jsonPath("$.curr").isEqualTo(price2Currency)
                .jsonPath("$.priority").doesNotExist();
    }


    @Test
    void getPrices_Given_two_prices_dates_overlap_same_product_different_brand_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long price2BrandId = 2L;
        Long productId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates overlap, same brand and different product
        PriceEntity price1 = PriceEntity.builder()
                .id("1")
                .productId(productId)
                .brandId(5L)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2019-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2019-07-14T23:59:59"))
                .priority(1)
                .price(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();
        // should find this one
        PriceEntity price2 = PriceEntity.builder()
                .id("2")
                .productId(productId)
                .brandId(price2BrandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .price(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1).block();
        priceRepository.save(price2).block();

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", price2StartDate.plusMinutes(5).toString())
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", price2BrandId)
                        .build())
                .exchange()

                // Then:
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand_id").isEqualTo(price2BrandId.intValue())
                .jsonPath("$.start_date").isEqualTo(price2StartDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.end_date").isEqualTo(price2EndDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.price_list").isEqualTo(price2PriceListId)
                .jsonPath("$.product_id").isEqualTo(productId.intValue())
                .jsonPath("$.price").isEqualTo(price2Amount)
                .jsonPath("$.curr").isEqualTo(price2Currency)
                .jsonPath("$.priority").doesNotExist();
    }

    @Test
    void getPrices_Given_two_prices_dates_do_not_overlap_different_product_different_brand_Then_return_right_one() {
        // Given:
        LocalDateTime price2StartDate = LocalDateTime.parse("2019-07-01T00:00:00");
        LocalDateTime price2EndDate = LocalDateTime.parse("2019-12-31T23:59:59");
        Long price2BrandId = 2L;
        Long price2productId = 4L;
        Long price2PriceListId = 4L;
        BigDecimal price2Amount = BigDecimal.valueOf(28.50);
        String price2Currency = "EUR";

        // Dates do not overlap, different brand and different product
        PriceEntity price1 = PriceEntity.builder()
                .id("1")
                .productId(9L)
                .brandId(5L)
                .priceListId(3L)
                .startDate(LocalDateTime.parse("2018-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2018-07-14T23:59:59"))
                .priority(1)
                .price(BigDecimal.valueOf(25.75))
                .currency("EUR")
                .build();
        // should find this one
        PriceEntity price2 = PriceEntity.builder()
                .id("2")
                .productId(price2productId)
                .brandId(price2BrandId)
                .priceListId(price2PriceListId)
                .startDate(price2StartDate)
                .endDate(price2EndDate)
                .priority(2)
                .price(price2Amount)
                .currency(price2Currency)
                .build();

        // Insert data
        priceRepository.save(price1).block();
        priceRepository.save(price2).block();

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", price2StartDate.plusMinutes(5).toString())
                        .queryParam("product_id", price2productId)
                        .queryParam("brand_id", price2BrandId)
                        .build())
                .exchange()

                // Then:
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.brand_id").isEqualTo(price2BrandId.intValue())
                .jsonPath("$.start_date").isEqualTo(price2StartDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.end_date").isEqualTo(price2EndDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.price_list").isEqualTo(price2PriceListId)
                .jsonPath("$.product_id").isEqualTo(price2productId.intValue())
                .jsonPath("$.price").isEqualTo(price2Amount)
                .jsonPath("$.curr").isEqualTo(price2Currency)
                .jsonPath("$.priority").doesNotExist();
    }

}
