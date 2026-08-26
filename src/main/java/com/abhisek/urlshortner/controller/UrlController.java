package com.abhisek.urlshortner.controller;

import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.Service.UrlService;
import com.abhisek.urlshortner.dto.CreateUrlRequest;
import com.abhisek.urlshortner.dto.UrlResponse;
import com.abhisek.urlshortner.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/urls")
public class UrlController {

    private final UrlService urlService;
    private final UserRepository userRepository;

    public UrlController(
            UrlService urlService,
            UserRepository userRepository) {

        this.urlService = urlService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public UrlResponse createUrl(
            @RequestBody CreateUrlRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        return urlService.createShortUrl(
                request,
                user
        );
    }
}