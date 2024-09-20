package com.cap.senior.prices_api.adapter.out.database.mongo.adapter;

import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import com.cap.senior.prices_api.adapter.out.database.mongo.repository.ReactiveMongoPriceRepository;
import com.cap.senior.prices_api.domain.model.Price;
import com.cap.senior.prices_api.domain.ports.out.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private final ReactiveMongoPriceRepository reactiveMongoPersonRepository;

    @Override
    public Flux<Price> getPricesByDateProductAndBrand(LocalDateTime date, Long productId, Long brandId) {
        /*
         MongoDB query implementation.
         Prices which end date matches the search date won't be discarded here (that's application logic)
         */
        return reactiveMongoPersonRepository.
                findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(brandId, productId, date, date)
                .map(PriceEntity::toDomain);
    }
}
