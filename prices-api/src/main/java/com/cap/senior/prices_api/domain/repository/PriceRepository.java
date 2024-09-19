package com.cap.senior.prices_api.domain.repository;

import com.cap.senior.prices_api.domain.model.Price;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface PriceRepository {

    /**
     * Get prices which start and end dates applied for the given date, product id and brand id.
     * There could be more than one, since priority is used in case of collision
     *
     * @param date
     * @param productId
     * @param brandId
     * @return
     */
    Flux<Price> getPricesByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId);

}
