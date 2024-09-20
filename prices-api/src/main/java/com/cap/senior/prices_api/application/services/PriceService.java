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
        return Mono.empty();
    }
}
