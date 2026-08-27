package com.abhisek.urlshortner.cofig;

import com.abhisek.urlshortner.Service.CustomUserDetailsService;
import com.abhisek.urlshortner.Service.JwtService;
import com.abhisek.urlshortner.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class JwtAuthenticationFilterTest {

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsAuthenticationWhenTokenValid() throws Exception {
        JwtService jwtService = Mockito.mock(JwtService.class);
        CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);

        when(jwtService.extractUsername("tok")).thenReturn("bob");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("bob")).thenReturn(userDetails);
        when(jwtService.isTokenValid("tok", userDetails)).thenReturn(true);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer tok");

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        filter.doFilterInternal(req, resp, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, resp);
    }

    @Test
    void proceedsWhenHeaderMissing() throws Exception {
        JwtService jwtService = Mockito.mock(JwtService.class);
        CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn(null);

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        filter.doFilterInternal(req, resp, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, resp);
    }

    @Test
    void continuesOnInvalidTokenException() throws Exception {
        JwtService jwtService = Mockito.mock(JwtService.class);
        CustomUserDetailsService userDetailsService = Mockito.mock(CustomUserDetailsService.class);

        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        when(req.getHeader("Authorization")).thenReturn("Bearer bad");
        when(jwtService.extractUsername("bad")).thenThrow(new RuntimeException("invalid"));

        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
        filter.doFilterInternal(req, resp, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(req, resp);
    }
}
