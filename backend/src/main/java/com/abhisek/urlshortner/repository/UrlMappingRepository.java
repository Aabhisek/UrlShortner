package com.abhisek.urlshortner.repository;

import com.abhisek.urlshortner.Entiry.UrlMapping;
import com.abhisek.urlshortner.Entiry.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByShortUrl(String shortUrl);

    boolean existsByShortUrl(String shortUrl);

    List<UrlMapping> findByUser(User user);

    long countByUser(User user);
}
