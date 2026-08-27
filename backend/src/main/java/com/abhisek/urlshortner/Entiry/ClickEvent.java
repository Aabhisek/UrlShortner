package com.abhisek.urlshortner.Entiry;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ClickEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime clickDate;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Url_mapping_id")
    private UrlMapping urlMapping;
}
