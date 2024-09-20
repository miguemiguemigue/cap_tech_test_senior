package com.cap.senior.prices_api.domain.ports.in;

import com.cap.senior.prices_api.domain.model.Price;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface GetPriceUseCase {

    Flux<Price> getPriceByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId);

}
