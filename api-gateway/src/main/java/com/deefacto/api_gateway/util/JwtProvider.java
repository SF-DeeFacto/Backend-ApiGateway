package com.deefacto.api_gateway.util;

import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    
    public boolean validateToken(String token) {
        // TODO: Implement JWT token validation logic
        return true;
    }

    public String getUserIdFromToken(String token) {
        // TODO: Implement JWT token parsing logic
        return "dummyUserId";
    }
}
