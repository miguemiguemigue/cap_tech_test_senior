package com.cap.senior.prices_api.domain.repository;

import com.cap.senior.prices_api.domain.model.Price;
import reactor.core.publisher.Mono;

public interface PriceRepository {

    Mono<Price> getPrice();

}
