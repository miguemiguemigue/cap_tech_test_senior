package com.cap.senior.prices_api.application.usecases;

import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.in.GetPriceUseCase;
import com.cap.senior.prices_api.domain.ports.out.PriceRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class GetPriceUseCaseImpl implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Flux<Price> getPriceByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        return priceRepositoryPort.getPricesByDateProductAndBrand(date, productId, brandId);
    }
}
