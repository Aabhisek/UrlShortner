package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.Entiry.UrlMapping;
import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.dto.CreateUrlRequest;
import com.abhisek.urlshortner.dto.UrlResponse;
import com.abhisek.urlshortner.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UrlServiceTest {

    @Test
    void createShortUrlSavesAndReturnsResponse() {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);
        when(repo.existsByShortUrl(any())).thenReturn(false);

        UrlMapping saved = new UrlMapping();
        saved.setId(1L);
        saved.setOriginalUrl("https://example.com");
        saved.setShortUrl("ABC123");
        saved.setClickCount(0);

        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        UrlService service = new UrlService(repo);

        CreateUrlRequest req = new CreateUrlRequest("https://example.com");
        User user = new User();
        user.setId(2L);

        UrlResponse resp = service.createShortUrl(req, user);

        assertNotNull(resp);
        assertEquals(1L, resp.id());
        assertEquals("https://example.com", resp.originalUrl());
        assertEquals(0, resp.clickCount());
        assertNotNull(resp.shortUrl());
        assertEquals(6, resp.shortUrl().length());
    }

    @Test
    void generateUniqueShortCodeHandlesCollision() {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);

        // First exists check returns true (collision), then false
        when(repo.existsByShortUrl(any())).thenReturn(true).thenReturn(false);

        UrlMapping saved = new UrlMapping();
        saved.setId(3L);
        saved.setOriginalUrl("https://x.com");
        saved.setShortUrl("ZZZZZZ");
        saved.setClickCount(0);

        when(repo.save(any(UrlMapping.class))).thenReturn(saved);

        UrlService service = new UrlService(repo);
        CreateUrlRequest req = new CreateUrlRequest("https://x.com");
        User user = new User();

        UrlResponse resp = service.createShortUrl(req, user);

        assertNotNull(resp);
        assertEquals(3L, resp.id());
    }
}
