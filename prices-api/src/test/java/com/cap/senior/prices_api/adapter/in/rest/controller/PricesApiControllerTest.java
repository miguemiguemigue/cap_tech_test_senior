package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.application.services.PriceService;
import com.cap.senior.prices_api.domain.model.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

@WebFluxTest(PricesApiController.class)
class PricesApiControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private PriceService priceService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    @Test
    void getPrices_Given_valid_arguments_Then_return_price() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        LocalDateTime priceStartDate = LocalDateTime.of(2024, 9, 20, 0, 0, 0);
        LocalDateTime priceEndDate = LocalDateTime.of(2024, 9, 22, 0, 0, 0);
        Long priceListId = 1L;
        int priority = 2;
        BigDecimal priceAmount = BigDecimal.valueOf(20);
        String currency = "EUR";
        Price price = Price.builder()
                .brandId(brandId)
                .productId(productId)
                .priceListId(priceListId)
                .startDate(priceStartDate)
                .endDate(priceEndDate)
                .priority(priority)
                .priceAmount(priceAmount)
                .currency(currency)
                .build();

        Mono<Price> monoPrice = Mono.just(price);

        when(priceService.findByDateProductAndBrand(date, productId, brandId)).thenReturn(monoPrice);

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
                .jsonPath("$.start_date").isEqualTo(priceStartDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.end_date").isEqualTo(priceEndDate.format(DATE_TIME_FORMATTER))
                .jsonPath("$.price_list").isEqualTo(priceListId.intValue())
                .jsonPath("$.product_id").isEqualTo(productId.intValue())
                .jsonPath("$.price").isEqualTo(priceAmount.doubleValue())
                .jsonPath("$.curr").isEqualTo(currency)
                // keep priority out of api response
                .jsonPath("$.priority").doesNotExist();

        verify(priceService).findByDateProductAndBrand(eq(date), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_date_Then_return_bad_request() {
        // Given:
        String invalidDate = "invalidDate";
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", invalidDate)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_date_Then_return_bad_request() {
        // Given:
        String invalidDate = null;
        Long productId = 1L;
        Long brandId = 1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", invalidDate)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_product_id_Then_return_bad_request() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = -1L;
        Long brandId = 1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_product_id_Then_return_bad_request() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = null;
        Long brandId = 1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_invalid_brand_id_Then_return_bad_request() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = -1L;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

    @Test
    void getPrices_Given_null_brand_id_Then_return_bad_request() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = null;

        // When:
        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/prices")
                        .queryParam("date", date)
                        .queryParam("product_id", productId)
                        .queryParam("brand_id", brandId)
                        .build())
                .exchange()
                // Then:
                .expectStatus().isBadRequest();

        verify(priceService, never()).findByDateProductAndBrand(any(), eq(productId), eq(brandId));
    }

}