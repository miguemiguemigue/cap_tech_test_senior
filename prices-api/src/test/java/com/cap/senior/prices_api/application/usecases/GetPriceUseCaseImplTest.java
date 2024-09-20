package com.cap.senior.prices_api.application.usecases;

import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.out.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPriceUseCaseImplTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private GetPriceUseCaseImpl getPriceUseCase;

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_one_price_Then_return_it() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;
        Price price = Price.builder()
                .startDate(date.minusDays(1))
                .endDate(date.plusDays(1))
                .productId(productId)
                .brandId(brandId)
                .build();

        when(priceRepositoryPort.getPricesByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.just(price));

        // When
        Flux<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        assertThat(result.collectList().block()).containsExactly(price);
        verify(priceRepositoryPort).getPricesByDateProductAndBrand(date, productId, brandId);
    }

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_no_prices_Then_return_empty() {
        // Given:
        LocalDateTime date = LocalDateTime.now();
        Long productId = 1L;
        Long brandId = 1L;

        when(priceRepositoryPort.getPricesByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.empty());

        // When
        Flux<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        StepVerifier.create(result)
                .expectComplete()
                .verify();

        verify(priceRepositoryPort).getPricesByDateProductAndBrand(date, productId, brandId);
    }

    @Test
    public void getPriceByDateProductAndBrand_Given_repository_return_multiple_prices_Then_return_all_prices() {
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
                .priority(2)
                .priceAmount(BigDecimal.valueOf(30.00))
                .currency("EUR")
                .build();

        when(priceRepositoryPort.getPricesByDateProductAndBrand(date, productId, brandId))
                .thenReturn(Flux.just(price1, price2));

        // When
        Flux<Price> result = getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId);

        // Then
        // This component shouldn't apply the priority logic here, It will be done in PriceService
        StepVerifier.create(result)
                .expectNext(price1)
                .expectNext(price2)
                .verifyComplete();

        verify(priceRepositoryPort).getPricesByDateProductAndBrand(date, productId, brandId);
    }

}