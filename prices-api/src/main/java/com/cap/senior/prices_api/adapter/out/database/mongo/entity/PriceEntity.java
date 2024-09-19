package com.cap.senior.prices_api.adapter.out.database.mongo.entity;

import com.cap.senior.prices_api.domain.model.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "Price")
@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class PriceEntity {

    @Id
    private String id;
    @Field("brand_id")
    private Long brandId;
    @Field("start_date")
    private LocalDateTime startDate;
    @Field("end_date")
    private LocalDateTime endDate;
    @Field("price_list_id")
    private Long priceListId;
    @Field("product_id")
    private Long productId;
    private int priority;
    private BigDecimal price;
    @Field("currency")
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
                .price(priceEntity.getPrice())
                .currency(priceEntity.getCurrency())
                .build();
    }

}
