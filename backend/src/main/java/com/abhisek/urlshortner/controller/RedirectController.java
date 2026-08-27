package com.abhisek.urlshortner.controller;

import com.abhisek.urlshortner.Entiry.ClickEvent;
import com.abhisek.urlshortner.Entiry.UrlMapping;
import com.abhisek.urlshortner.repository.ClickEventRepository;
import com.abhisek.urlshortner.repository.UrlMappingRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
public class RedirectController {

    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    public RedirectController(
            UrlMappingRepository urlMappingRepository,
            ClickEventRepository clickEventRepository) {

        this.urlMappingRepository = urlMappingRepository;
        this.clickEventRepository = clickEventRepository;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(
            @PathVariable String shortCode) {

        UrlMapping urlMapping =
                urlMappingRepository
                        .findByShortUrl(shortCode)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Short URL not found"
                                )
                        );

        urlMapping.setClickCount(
                urlMapping.getClickCount() + 1
        );

        urlMappingRepository.save(urlMapping);

        ClickEvent clickEvent = new ClickEvent();

        clickEvent.setClickDate(
                LocalDateTime.now()
        );

        clickEvent.setUrlMapping(urlMapping);

        clickEventRepository.save(clickEvent);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.LOCATION,
                urlMapping.getOriginalUrl()
        );

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }
}

