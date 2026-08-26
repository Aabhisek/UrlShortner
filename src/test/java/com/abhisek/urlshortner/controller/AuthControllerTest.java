package com.abhisek.urlshortner.controller;

import com.abhisek.urlshortner.dto.AuthResponse;
import com.abhisek.urlshortner.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class AuthControllerTest {

    @Test
    void loginReturnsTokenOnAuthenticate() {
        AuthenticationManager authManager = Mockito.mock(AuthenticationManager.class);
        Authentication auth = Mockito.mock(Authentication.class);
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
        com.abhisek.urlshortner.repository.UserRepository userRepository = Mockito.mock(com.abhisek.urlshortner.repository.UserRepository.class);
        com.abhisek.urlshortner.Service.JwtService jwtService = Mockito.mock(com.abhisek.urlshortner.Service.JwtService.class);

        Mockito.when(authManager.authenticate(any())).thenReturn(auth);
        Mockito.when(auth.getPrincipal()).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(userDetails)).thenReturn("tok123");

        AuthController controller = new AuthController(authManager, userRepository, passwordEncoder, jwtService);

        AuthResponse resp = controller.login(new LoginRequest("bob", "pw"));
        assertNotNull(resp);
        assertEquals("tok123", resp.token());
    }
}
