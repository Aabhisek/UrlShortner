package com.abhisek.urlshortner.controller;

import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.Service.UrlService;
import com.abhisek.urlshortner.dto.CreateUrlRequest;
import com.abhisek.urlshortner.dto.UrlResponse;
import com.abhisek.urlshortner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UrlControllerTest {

    @Test
    void createUrlReturnsResponse() {
        UrlService urlService = Mockito.mock(UrlService.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        User user = new User();
        user.setId(5L);
        user.setUsername("joe");

        when(userRepository.findByUsername("joe")).thenReturn(Optional.of(user));

        UrlResponse resp = new UrlResponse(10L, "https://a.com", "ABC123", 0, LocalDateTime.now());
        when(urlService.createShortUrl(Mockito.any(CreateUrlRequest.class), Mockito.eq(user))).thenReturn(resp);

        UrlController controller = new UrlController(urlService, userRepository);

        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.getName()).thenReturn("joe");

        UrlResponse out = controller.createUrl(new CreateUrlRequest("https://a.com"), auth);
        assertNotNull(out);
        assertEquals(10L, out.id());
        assertEquals("ABC123", out.shortUrl());
    }
}
