package com.abhisek.urlshortner.repository;

import com.abhisek.urlshortner.Entiry.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {
}
