package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.adapter.in.rest.dto.PriceResponse;
import com.cap.senior.prices_api.adapter.in.rest.mapper.PriceResponseMapper;
import com.cap.senior.prices_api.application.services.PriceService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<PriceResponse> pricesGet(
            LocalDateTime date,
            Long productId,
            Long brandId,
            final ServerWebExchange exchange
    ) {
        return priceService
                .findByDateProductAndBrand(date, productId, brandId)
                .map(PriceResponseMapper::fromDomain)
                .doOnSuccess(priceResponse -> logger.info("Retrieved price for productId: {}, brandId: {}", productId, brandId))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Price not found")));
    }
}
