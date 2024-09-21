package com.cap.senior.prices_api.adapter.out.database.mongo.entity;

import com.cap.senior.prices_api.adapter.out.database.mongo.config.MongoConfig;
import com.cap.senior.prices_api.domain.model.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = MongoConfig.PRICE_COLLECTION_NAME)
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class PriceEntity {

    @Id
    private String id;
    private Long brandId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long priceListId;
    private Long productId;
    private int priority;
    private BigDecimal price;
    private String currency;

    public static Price toDomain(PriceEntity priceEntity) {
        if (priceEntity == null) {
            return null;
        }
        return Price.builder()
                .brandId(priceEntity.getBrandId())
                .productId(priceEntity.getProductId())
                .priceListId(priceEntity.getPriceListId())
                .startDate(priceEntity.getStartDate())
                .endDate(priceEntity.getEndDate())
                .priority(priceEntity.getPriority())
                .priceAmount(priceEntity.getPrice())
                .currency(priceEntity.getCurrency())
                .build();
    }

}
