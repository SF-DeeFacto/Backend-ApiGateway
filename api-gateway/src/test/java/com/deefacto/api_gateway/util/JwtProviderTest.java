package com.deefacto.api_gateway.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
    }

    @Test
    void testValidateToken() {
        // Given
        String token = "test.jwt.token";

        // When
        boolean result = jwtProvider.validateToken(token);

        // Then
        assertTrue(result, "현재는 모든 토큰이 유효하다고 반환하므로 true여야 함");
    }

    @Test
    void testValidateTokenWithNull() {
        // Given
        String token = null;

        // When
        boolean result = jwtProvider.validateToken(token);

        // Then
        assertTrue(result, "현재는 null 토큰도 유효하다고 반환하므로 true여야 함");
    }

    @Test
    void testValidateTokenWithEmptyString() {
        // Given
        String token = "";

        // When
        boolean result = jwtProvider.validateToken(token);

        // Then
        assertTrue(result, "현재는 빈 문자열도 유효하다고 반환하므로 true여야 함");
    }

    @Test
    void testGetUserIdFromToken() {
        // Given
        String token = "test.jwt.token";

        // When
        String userId = jwtProvider.getUserIdFromToken(token);

        // Then
        assertEquals("dummyUserId", userId, "현재는 항상 'dummyUserId'를 반환해야 함");
    }

    @Test
    void testGetUserIdFromTokenWithNull() {
        // Given
        String token = null;

        // When
        String userId = jwtProvider.getUserIdFromToken(token);

        // Then
        assertEquals("dummyUserId", userId, "현재는 null 토큰도 'dummyUserId'를 반환해야 함");
    }

    @Test
    void testGetUserIdFromTokenWithEmptyString() {
        // Given
        String token = "";

        // When
        String userId = jwtProvider.getUserIdFromToken(token);

        // Then
        assertEquals("dummyUserId", userId, "현재는 빈 문자열도 'dummyUserId'를 반환해야 함");
    }
} 