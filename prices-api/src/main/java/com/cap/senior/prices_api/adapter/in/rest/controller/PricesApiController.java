package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.adapter.in.rest.dto.PriceResponse;
import com.cap.senior.prices_api.adapter.in.rest.mapper.PriceResponseMapper;
import com.cap.senior.prices_api.application.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class PricesApiController implements PricesApi {

    private final PriceService priceService;
    private static final Logger logger = LoggerFactory.getLogger(PricesApiController.class);

    @Override
    public Mono<ResponseEntity<PriceResponse>> pricesGet(
            LocalDateTime date,
            Long productId,
            Long brandId,
            final ServerWebExchange exchange
    ) {
        return priceService
                .findByDateProductAndBrand(date, productId, brandId)
                .map(price -> {
                    PriceResponse priceResponse = PriceResponseMapper.fromDomain(price);
                    logger.info("Retrieved price for productId: {}, brandId: {}", productId, brandId);
                    return ResponseEntity.ok(priceResponse);
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)));
    }
}
