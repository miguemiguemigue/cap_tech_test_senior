package com.cap.senior.prices_api.adapter.out.database.mongo.repository;

import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ReactiveMongoPriceRepository extends ReactiveMongoRepository<PriceEntity, Long> {

    Flux<PriceEntity> findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long brandId, Long productId, LocalDateTime startDate, LocalDateTime endDate
    );
}
