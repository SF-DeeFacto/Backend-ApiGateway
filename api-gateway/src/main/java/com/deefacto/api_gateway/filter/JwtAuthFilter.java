package com.deefacto.api_gateway.filter;

import com.deefacto.api_gateway.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * JWT 인증 필터
 * 
 * 모든 API 요청에 대해 JWT 토큰을 검증하고, 
 * 인증된 사용자 정보를 헤더에 추가하여 하위 서비스로 전달하는 필터
 * 
 * 주요 기능:
 * - JWT 토큰 검증
 * - 인증 제외 경로 처리 (로그인, 회원가입)
 * - 사용자 정보 헤더 추가 (직원ID, 역할)
 * - 인증 실패 시 JSON 형식 에러 응답
 * 
 * @author API Gateway Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor  // final 필드들을 매개변수로 받는 생성자 자동 생성
@Slf4j
public class JwtAuthFilter implements GlobalFilter, Ordered {

    /**
     * JWT 토큰 처리를 담당하는 유틸리티 클래스
     * 토큰 검증, 파싱 등의 기능 제공
     */
    private final JwtProvider jwtProvider;

    /**
     * 필터 실행 순서를 정의
     * 낮은 숫자가 먼저 실행됨 (다른 필터보다 우선 실행)
     */
    @Override
    public int getOrder() {
        return -100; // 다른 필터보다 먼저 실행
    }

    /**
     * 모든 API 요청에 대해 실행되는 필터 메서드
     * 
     * 처리 순서:
     * 1. 인증 제외 경로 확인
     * 2. Authorization 헤더 확인
     * 3. JWT 토큰 검증
     * 4. 사용자 정보 추출 및 헤더 추가
     * 5. 하위 서비스로 요청 전달
     * 
     * @param exchange HTTP 요청/응답 정보를 담고 있는 객체
     * @param chain 다음 필터 또는 서비스로 요청을 전달하는 체인
     * @return Mono<Void> 비동기 처리 결과
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // HTTP 요청에서 Authorization 헤더 추출
        // 예: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 요청 경로 추출 (인증 제외 경로 확인용)
        String path = exchange.getRequest().getURI().getPath();
        
        // 디버깅을 위한 로그 추가
        log.info("JwtAuthFilter - 요청 경로: {}, Authorization 헤더: {}", path, authHeader);

        // 인증이 필요하지 않은 경로들 (로그인, 회원가입)
        // 이 경로들은 JWT 토큰 없이도 접근 가능
        if (path.startsWith("/auth/login")) {
            log.info("JwtAuthFilter - 인증 제외 경로: {}", path);
            // 인증 없이 바로 다음 필터/서비스로 요청 전달
            return chain.filter(exchange);
        }

        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않는 경우
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("JwtAuthFilter - 인증 헤더 없음 또는 잘못된 형식: {}", authHeader);
            return onError(exchange, "인증 헤더가 없거나 잘못된 형식입니다", HttpStatus.UNAUTHORIZED);
        }
        
        // "Bearer " 접두사를 제거하여 순수 JWT 토큰만 추출
        String token = authHeader.replace("Bearer ", "");
        
        // JWT 토큰 유효성 검증 (서명, 만료시간 등)
        if (!jwtProvider.validateToken(token)) {
            log.warn("JwtAuthFilter - 유효하지 않은 토큰: {}", token);
            return onError(exchange, "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);
        }

        // JWT 토큰에서 사용자 정보 추출
        Long userId = jwtProvider.getUserIdFromToken(token);            // 유저 ID (불변)
        String employeeId = jwtProvider.getEmployeeIdFromToken(token);  // 직원 ID
        
        log.info("JwtAuthFilter - 토큰 검증 성공: employeeId={}, userId={}", employeeId, userId);

        // 원본 요청에 사용자 정보를 헤더로 추가하여 새로운 요청 생성
        // 하위 서비스에서는 이 헤더를 통해 사용자 정보를 확인할 수 있음
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                            .header("X-Employee-Id", employeeId)  // 직원 ID 헤더
                                            .header("X-User-Id", String.valueOf(userId)) // 유저 ID 헤더
                                            .build();

        // 수정된 요청을 다음 필터/서비스로 전달
        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    /**
     * 인증 실패 시 JSON 형식의 에러 응답을 생성하는 메서드
     * 
     * @param exchange HTTP 요청/응답 정보
     * @param err 에러 메시지
     * @param httpStatus HTTP 상태 코드 (예: 401 Unauthorized)
     * @return Mono<Void> 비동기 처리 결과
     */

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        String errMsg = "{\"error\": \"" + err + "\"}";
        byte[] bytes = errMsg.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);

        exchange.getResponse().setStatusCode(httpStatus);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        exchange.getResponse().getHeaders().add("Content-Length", String.valueOf(bytes.length));

        // writeWith만 반환 -> 여기서 완료 처리까지 됨
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
