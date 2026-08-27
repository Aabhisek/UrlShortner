package com.abhisek.urlshortner.dto;

import java.time.LocalDateTime;

public record UrlResponse(Long id,
                          String originalUrl,
                          String shortUrl,
                          long clickCount,
                          LocalDateTime createdDate) {
}
