package com.deefacto.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 토큰 처리 전용 유틸리티 클래스
 * 
 * 주요 기능:
 * - JWT 토큰 검증 (서명, 만료시간 확인)
 * - JWT 토큰에서 사용자 정보 추출 (직원ID, 역할)
 * - JWT 서명 키 관리
 * 
 * @author RAFA
 * @version 1.0
 */
@Component
@Slf4j  // Lombok을 통한 로깅 기능 자동 생성
@RequiredArgsConstructor  // final 필드들을 매개변수로 받는 생성자 자동 생성
public class JwtProvider {
    
    /**
     * Spring의 Environment 객체를 주입받아 설정 파일의 값을 읽어옴
     * application.yml, application-dev.yml, application-prod.yml 등의 설정값 접근 가능
     */
    private final Environment environment;

    /**
     * JWT 토큰의 유효성을 검증하는 메서드
     * 
     * 검증 항목:
     * 1. 토큰 서명 검증 (위조 방지)
     * 2. 토큰 만료시간 검증 (유효기간 확인)
     * 
     * @param token 검증할 JWT 토큰 문자열
     * @return true: 유효한 토큰, false: 유효하지 않은 토큰
     */
    public boolean validateToken(String token) {
        try {
            // JWT 토큰을 파싱하여 Claims(페이로드) 추출
            Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())  // 서명 키로 검증
                .build()
                .parseSignedClaims(token)     // 서명된 토큰 파싱
                .getPayload();                // 페이로드(Claims) 추출
                
            // 토큰의 만료시간이 현재시간보다 이전인지 확인
            // 만료시간이 현재시간보다 이전이면 true 반환 (만료됨)
            // 만료시간이 현재시간보다 이후이면 false 반환 (유효함)
            return !claims.getExpiration().before(new Date());
            
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 시 (잘못된 형식, 만료됨, 서명 불일치 등)
            log.error("JWT 토큰 검증 중 오류 발생: {}", e.getMessage(), e);
            return false;  // 검증 실패로 처리
        }
    }

    /**
     * JWT 토큰에서 직원 ID를 추출하는 메서드
     * 
     * 토큰의 페이로드에서 "employeeId" claim을 추출하여 반환
     * 
     * @param token JWT 토큰 문자열
     * @return 직원 ID 문자열, 파싱 실패 시 null
     */
    public String getEmployeeIdFromToken(String token) {
        try {
            // JWT 토큰을 파싱하여 Claims 추출
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())  // 서명 키로 검증
                    .build()
                    .parseSignedClaims(token)     // 서명된 토큰 파싱
                    .getPayload();                // 페이로드 추출
            
            // Claims에서 "employeeId" 키의 값을 String 타입으로 추출
            return claims.get("employeeId", String.class);
            
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 시
            log.error("JWT 토큰에서 직원 ID 추출 중 오류 발생: {}", e.getMessage(), e);
            return null;  // 추출 실패 시 null 반환
        }
    }

    /**
     * JWT 토큰에서 사용자 역할을 추출하는 메서드
     * 
     * 토큰의 페이로드에서 "role" claim을 추출하여 반환
     * 
     * @param token JWT 토큰 문자열
     * @return 사용자 역할 문자열 (예: ADMIN, USER), 파싱 실패 시 null
     */
    public String getRoleFromToken(String token) {
        try {
            // JWT 토큰을 파싱하여 Claims 추출
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())  // 서명 키로 검증
                    .build()
                    .parseSignedClaims(token)     // 서명된 토큰 파싱
                    .getPayload();                // 페이로드 추출
                    
            // Claims에서 "role" 키의 값을 String 타입으로 추출
            return claims.get("role", String.class);
            
        } catch (Exception e) {
            // 토큰 파싱 중 오류 발생 시
            log.error("JWT 토큰에서 사용자 역할 추출 중 오류 발생: {}", e.getMessage(), e);
            return null;  // 추출 실패 시 null 반환
        }
    }

    /**
     * JWT 토큰 서명에 사용할 비밀키를 생성하는 메서드
     * 
     * 설정 파일(application.yml)에서 "jwt.secret-key" 값을 읽어와서
     * Base64 디코딩 후 HMAC-SHA256 알고리즘용 SecretKey 객체로 변환
     * 
     * @return JWT 서명용 SecretKey 객체
     */
    private SecretKey getSigningKey() {
        // 설정 파일에서 JWT 시크릿 키를 읽어옴
        // 환경별로 다른 키 사용 가능 (dev/prod)
        String secret = environment.getProperty("jwt.secret-key");
        
        // Base64로 인코딩된 문자열을 디코딩하여 바이트 배열로 변환
        // Base64.getDecoder(): Base64 디코더 인스턴스 생성
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        
        // 바이트 배열을 HMAC-SHA256 키로 변환
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
