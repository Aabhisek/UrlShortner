package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.repository.UrlMappingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UrlServicePrivateTest {

    @Test
    void generateRandomCodeProducesCorrectLength() throws Exception {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);
        UrlService s = new UrlService(repo);

        Method m = UrlService.class.getDeclaredMethod("generateRandomCode");
        m.setAccessible(true);

        String code = (String) m.invoke(s);
        assertNotNull(code);
        assertEquals(6, code.length());
    }

    @Test
    void generateUniqueShortCodeHandlesCollisionsViaRepo() throws Exception {
        UrlMappingRepository repo = Mockito.mock(UrlMappingRepository.class);
        // First call collision, then unique
        when(repo.existsByShortUrl(Mockito.anyString())).thenReturn(true).thenReturn(false);

        UrlService s = new UrlService(repo);

        Method m = UrlService.class.getDeclaredMethod("generateUniqueShortCode");
        m.setAccessible(true);

        String code = (String) m.invoke(s);
        assertNotNull(code);
        assertEquals(6, code.length());
    }
}
