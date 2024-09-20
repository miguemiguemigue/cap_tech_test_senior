package com.cap.senior.prices_api.application.services;

import com.cap.senior.prices_api.adapter.out.database.mongo.mongock.config.PriceInitializerChangeUnit;
import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.in.GetPriceUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class PriceService {

    private final GetPriceUseCase getPriceUseCase;
    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    public Mono<Price> findByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {

        logger.info("Finding price for date: {}, productId: {}, brandId: {}", date, productId, brandId);

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

        // find prices, getting the top priority one
        return getPriceUseCase.getPriceByDateProductAndBrand(date, productId, brandId)
                .reduce((price1, price2) ->
                        // delegate domain logic
                        price1.hasHigherPriorityThan(price2) ? price1 : price2
                )
                .flatMap(price -> price != null ? Mono.just(price) : Mono.empty());
    }

}
