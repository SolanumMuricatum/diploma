package com.pepino.authservice.config;

import com.pepino.authservice.model.JwtAccess;
import com.pepino.authservice.model.JwtInternalService;
import com.pepino.authservice.model.JwtRefresh;
import com.pepino.authservice.service.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@NonNullApi
public class TokenFilter implements WebFilter {

    private final JwtAccess jwtAccess;
    private final JwtRefresh jwtRefresh;
    private final ReactiveUserDetailsService userDetailsService;
    private final TokenService tokenService;

    private final Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var path = exchange.getRequest().getURI().getPath();

        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        return processTokens(exchange, chain)
                .onErrorResume(e -> {
                    logger.error("Authentication failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }


    private boolean isPublicEndpoint(String path) {
        return List.of("/auth/login", "/auth/internal-service/public-key", "/auth/internal-service/token",
                        "/auth/ping", "/user/save", "/auth/access-token/public-key", "/album/getAll")
                .contains(path);
    }

    private Mono<Void> processTokens(ServerWebExchange exchange, WebFilterChain chain) {
        MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();

        // Получаем токены из cookies
        HttpCookie accessCookie = cookies.getFirst(jwtAccess.getName());
        HttpCookie refreshCookie = cookies.getFirst(jwtRefresh.getName());

        // Если нет access или refresh токена - отказываем
        if (accessCookie == null || refreshCookie == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return Mono.fromCallable(() -> jwtRefresh.getUsernameFromJwt(refreshCookie.getValue()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(refreshUser ->
                        validateAccessToken(accessCookie.getValue(), refreshUser, exchange, chain)
                )
                .onErrorResume(e -> {
                    logger.error("Token validation failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private Mono<Void> validateAccessToken(
            String token,
            String refreshUser,
            ServerWebExchange exchange,
            WebFilterChain chain
    ) {
        // Парсим токен и обрабатываем результат/ошибки реактивно
        return Mono.fromCallable(() -> parseAccessToken(token))
                .flatMap(tokenResult -> {
                    String username = tokenResult.username();
                    boolean expired = tokenResult.expired();

                    // Проверяем соответствие с refresh токеном
                    if (!username.equals(refreshUser)) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    // Загружаем пользователя из БД
                    return userDetailsService.findByUsername(username)
                            .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                            .flatMap(userDetails -> {
                                Authentication auth = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());

                                // Если токен просрочен — обновляем куки
                                if (expired) {
                                    ResponseCookie newAccessCookie = tokenService.createAccessTokenCookie(auth);
                                    ResponseCookie newRefreshCookie = tokenService.createRefreshTokenCookie(auth);
                                    exchange.getResponse().addCookie(newAccessCookie);
                                    exchange.getResponse().addCookie(newRefreshCookie);
                                }

                                // Пропускаем дальше с аутентификацией
                                return chain.filter(exchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                            });
                })
                .onErrorResume(e -> {
                    logger.error("Access token validation failed: {}", e.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private TokenResult parseAccessToken(String token) {
        try {
            String username = jwtAccess.getUsernameFromJwt(token);
            return new TokenResult(username, false); // Токен валиден
        } catch (ExpiredJwtException e) {
            String username = e.getClaims().getSubject();
            return new TokenResult(username, true); // Токен просрочен
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e); // Токен невалиден
        }
    }

    // Record для результата парсинга
    private record TokenResult(String username, boolean expired) {}
}
