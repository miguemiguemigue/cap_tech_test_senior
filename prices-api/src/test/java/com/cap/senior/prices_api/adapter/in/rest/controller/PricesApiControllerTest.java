package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.adapter.in.rest.dto.PriceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDateTime;

@WebFluxTest(PricesApiController.class)
class PricesApiControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    void getPrices_Given_valid_arguments_Then_return_price() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
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
                .expectStatus().isOk()
                .expectBody(PriceResponse.class).isEqualTo(new PriceResponse());
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
    }

}