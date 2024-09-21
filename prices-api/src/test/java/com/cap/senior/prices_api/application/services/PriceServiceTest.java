package com.cap.senior.prices_api.application.services;

import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.in.GetPriceUseCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceServiceTest {

    @Mock
    private GetPriceUseCase getPriceUseCase;

    @InjectMocks
    private PriceService priceService;

    @Test
    public void findByDateProductAndBrand_Given_null_date_Then_return_error() {
        // Given:
        LocalDateTime date = null;
        Long productId = 1L;
        Long brandId = 1L;

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Date must not be null and must be a valid date"))
                .verify();
    }

    @Test
    public void findByDateProductAndBrand_Given_null_product_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = null;
        Long brandId = 1L;

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("ProductId must be non-null and positive"))
                .verify();
    }

    @Test
    public void findByDateProductAndBrand_Given_negative_product_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = -1L;
        Long brandId = 1L;

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("ProductId must be non-null and positive"))
                .verify();
    }

    @Test
    public void findByDateProductAndBrand_Given_null_brand_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = null;

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("BrandId must be non-null and positive"))
                .verify();
    }

    @Test
    public void findByDateProductAndBrand_Given_negative_brand_id_Then_return_error() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = -1L;

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("BrandId must be non-null and positive"))
                .verify();
    }

    @Test
    void findByDateProductAndBrand_Given_empty_prices_Should_return_empty_price() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.empty());

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void findByDateProductAndBrand_Given_one_price_Should_return_it() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        Price price = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.just(price));

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectNext(price)
                .verifyComplete();

    }

    @Test
    public void findByDateProductAndBrand_Given_several_matching_prices_Should_return_max_priority_one() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        Price price1 = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(1)
                .priceAmount(BigDecimal.valueOf(25.45))
                .currency("EUR")
                .build();

        Price price2 = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .priority(2) // max priority
                .priceAmount(BigDecimal.valueOf(30.00))
                .currency("EUR")
                .build();

        when(getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.just(price1, price2));

        // When
        Mono<Price> result = priceService.findByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectNext(price2) // Should get max priority one
                .verifyComplete();
    }

}