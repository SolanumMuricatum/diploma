package com.pepino.authservice;

import com.pepino.authservice.config.TokenFilter;
import com.pepino.authservice.model.JwtInternalService;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RequiredArgsConstructor
@NonNullApi
public class AuthServiceApplication {
    private final Logger logger = LoggerFactory.getLogger(AuthServiceApplication.class);
    private final JwtInternalService jwtInternalService;
    @Value("${spring.application.name}")
    private String serviceName;

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/user/**")
                        .filters(f -> f.filter(addInternalAuthFilter()))
                        .uri("lb://user-service")
                )
                .route("album-service", r -> r
                        .path("/album/**")
                        .filters(f -> f.filter(addInternalAuthFilter()))
                        .uri("lb://album-service")
                )
                .build();
    }

    private GatewayFilter addInternalAuthFilter() {
        return (exchange, chain) -> Mono.fromCallable(() ->
                        jwtInternalService.generateInternalServiceToken(serviceName)
                )
                .flatMap(token -> {
                    // Модифицируем запрос
                    HttpHeaders requestHeaders = new HttpHeaders();
                    requestHeaders.addAll(exchange.getRequest().getHeaders());
                    requestHeaders.set("Authorization", "Bearer " + token);

                    var mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            return requestHeaders;
                        }
                    };

/*                    // Модифицируем ответ - УДАЛЯЕМ все CORS от сервисов - защита от дурака ахах
                    ServerHttpResponseDecorator mutatedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
                        @Override
                        public HttpHeaders getHeaders() {
                            HttpHeaders headers = super.getHeaders();

                            // Удаляем ВСЕ CORS-заголовки от downstream
                            headers.remove("Access-Control-Allow-Origin");
                            headers.remove("Access-Control-Allow-Methods");
                            headers.remove("Access-Control-Allow-Headers");
                            headers.remove("Access-Control-Allow-Credentials");
                            headers.remove("Access-Control-Expose-Headers");
                            headers.remove("Access-Control-Max-Age");
                            headers.remove("Vary"); // тоже может содержать Origin

                            return headers;
                        }
                    };*/

                    return chain.filter(exchange.mutate()
                            .request(mutatedRequest)
                           // .response(mutatedResponse)
                            .build());
                })
                .onErrorResume(e -> {
                    logger.error("Failed to generate internal token", e);
                    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return exchange.getResponse().setComplete();
                });
    }

}
