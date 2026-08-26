package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.Entiry.UrlMapping;
import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.dto.CreateUrlRequest;
import com.abhisek.urlshortner.dto.UrlResponse;
import com.abhisek.urlshortner.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class UrlService {

    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789";

    private static final int SHORT_CODE_LENGTH = 6;

    private final UrlMappingRepository urlMappingRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public UrlService(
            UrlMappingRepository urlMappingRepository) {

        this.urlMappingRepository = urlMappingRepository;
    }

    @Transactional
    public UrlResponse createShortUrl(
            CreateUrlRequest request,
            User user) {

        String shortCode = generateUniqueShortCode();

        UrlMapping urlMapping = new UrlMapping();

        urlMapping.setOriginalUrl(request.originalUrl());
        urlMapping.setShortUrl(shortCode);
        urlMapping.setClickCount(0);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setUser(user);

        UrlMapping saved =
                urlMappingRepository.save(urlMapping);

        return toResponse(saved);
    }

    private String generateUniqueShortCode() {

        String shortCode;

        do {
            shortCode = generateRandomCode();
        } while (
                urlMappingRepository.existsByShortUrl(shortCode)
        );

        return shortCode;
    }

    private String generateRandomCode() {

        StringBuilder code =
                new StringBuilder(SHORT_CODE_LENGTH);

        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {

            int index =
                    secureRandom.nextInt(CHARACTERS.length());

            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    private UrlResponse toResponse(
            UrlMapping urlMapping) {

        return new UrlResponse(
                urlMapping.getId(),
                urlMapping.getOriginalUrl(),
                urlMapping.getShortUrl(),
                urlMapping.getClickCount(),
                urlMapping.getCreatedDate()
        );
    }
}