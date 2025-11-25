package com.pepino.authservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ErrorResponseFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(ErrorResponseFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    // Логируем ошибку
                    logger.error("Error from downstream service: {} - Body: {}",
                            ex.getStatusCode(),
                            ex.getResponseBodyAsString());

                    ServerHttpResponse response = exchange.getResponse();

                    // Пробрасываем оригинальный статус и тело
                    response.setStatusCode(ex.getStatusCode());
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    DataBuffer buffer = response.bufferFactory()
                            .wrap(ex.getResponseBodyAsByteArray());

                    return response.writeWith(Mono.just(buffer));
                });
    }

    @Override
    public int getOrder() {
        return -1; // Высокий приоритет, выполняется рано
    }
}
