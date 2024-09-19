package com.cap.senior.prices_api.adapter.in.rest.controller;

import com.cap.senior.prices_api.adapter.in.rest.dto.PriceResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class PricesApiController implements PricesApi {

    @Override
    public Mono<PriceResponse> pricesGet(
            LocalDateTime date,
            Long productId,
            Long brandId,
            final ServerWebExchange exchange
    ) {
        PriceResponse priceResponse = new PriceResponse();
        return Mono.just(priceResponse);
    }
}
