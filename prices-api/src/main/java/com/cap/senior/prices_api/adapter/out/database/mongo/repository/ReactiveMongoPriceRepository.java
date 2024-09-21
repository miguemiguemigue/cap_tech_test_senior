package com.cap.senior.prices_api.adapter.out.database.mongo.repository;

import com.cap.senior.prices_api.adapter.out.database.mongo.entity.PriceEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface ReactiveMongoPriceRepository extends ReactiveMongoRepository<PriceEntity, Long> {

    /**
     * Get prices that match the following conditions:
     * - Price start date is greater than or equal to query start date
     * - Price end date is less than or equal to query end date
     * - Price product id matches
     * - Price brand id matches
     * @param brandId
     * @param productId
     * @param startDate
     * @param endDate
     * @return
     */
    Flux<PriceEntity> findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long brandId, Long productId, LocalDateTime startDate, LocalDateTime endDate
    );
}
