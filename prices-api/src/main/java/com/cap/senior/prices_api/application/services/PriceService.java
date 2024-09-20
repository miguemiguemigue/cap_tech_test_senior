package com.cap.senior.prices_api.application.services;

import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.in.GetPriceUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class PriceService {

    private final GetPriceUseCase getPriceUseCase;

    public Mono<Price> findByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {

        // check params
        if (date == null) {
            return Mono.error(new IllegalArgumentException("Date must not be null and must be a valid date"));
        }
        if (productId == null || productId <= 0) {
            return Mono.error(new IllegalArgumentException("ProductId must be non-null and positive"));
        }
        if (brandId == null || brandId <= 0) {
            return Mono.error(new IllegalArgumentException("BrandId must be non-null and positive"));
        }

        return getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId)
                .reduce((price1, price2) ->
                        price1.hasHigherPriorityThan(price2) ? price1 : price2
                )
                .flatMap(price -> price != null ? Mono.just(price) : Mono.empty());
    }

}
