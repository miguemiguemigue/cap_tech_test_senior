package com.cap.senior.prices_api.domain.ports.out;

import com.cap.senior.prices_api.domain.model.Price;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface PriceRepositoryPort {

    /**
     * Get prices that match the following conditions:
     * - Price start date is greater than or equal to search date
     * - Price end date is less than or equal to search date
     * - Price product id matches
     * - Price brand id matches
     *
     * @param date
     * @param productId
     * @param brandId
     * @return
     */
    Flux<Price> getPricesByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId);

}
