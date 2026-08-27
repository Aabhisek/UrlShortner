package com.abhisek.urlshortner.Service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private static final String SECRET = "5250db4bc177c193557c9cd1662c8d395691adf8f065eec771658ee98d741b20";
    private static final long EXPIRATION = 3600000L; // 1 hour

    @Test
    void generateAndValidateToken() {
        JwtService jwtService = new JwtService(SECRET, EXPIRATION);

        UserDetails user = new org.springframework.security.core.userdetails.User(
                "testuser",
                "pwd",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtService.generateToken(user);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);

        assertTrue(jwtService.isTokenValid(token, user));
    }
}
