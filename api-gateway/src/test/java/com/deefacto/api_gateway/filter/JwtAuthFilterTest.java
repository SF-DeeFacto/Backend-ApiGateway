package com.deefacto.api_gateway.filter;

import com.deefacto.api_gateway.util.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthFilterTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private GatewayFilterChain chain;

    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        jwtAuthFilter = new JwtAuthFilter();
        // Reflection을 사용하여 private 필드 주입
        try {
            java.lang.reflect.Field field = JwtAuthFilter.class.getDeclaredField("jwtProvider");
            field.setAccessible(true);
            field.set(jwtAuthFilter, jwtProvider);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFilterWithValidToken() {
        // Given
        String validToken = "Bearer valid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, validToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtProvider.validateToken("valid.jwt.token")).thenReturn(true);
        when(jwtProvider.getUserIdFromToken("valid.jwt.token")).thenReturn("user123");
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // When
        GatewayFilter filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testFilterWithInvalidToken() {
        // Given
        String invalidToken = "Bearer invalid.jwt.token";
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, invalidToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        when(jwtProvider.validateToken("invalid.jwt.token")).thenReturn(false);

        // When
        GatewayFilter filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        // 응답 상태 코드가 UNAUTHORIZED인지 확인
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void testFilterWithNoAuthorizationHeader() {
        // Given
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        GatewayFilter filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        // 응답 상태 코드가 UNAUTHORIZED인지 확인
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void testFilterWithMalformedToken() {
        // Given
        String malformedToken = "InvalidTokenFormat";
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, malformedToken)
                .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // When
        GatewayFilter filter = jwtAuthFilter.apply(new JwtAuthFilter.Config());
        Mono<Void> result = filter.filter(exchange, chain);

        // Then
        StepVerifier.create(result)
                .verifyComplete();
        
        // 응답 상태 코드가 UNAUTHORIZED인지 확인
        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
    }
} 