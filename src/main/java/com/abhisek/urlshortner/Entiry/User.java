package com.abhisek.urlshortner.Entiry;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String password;
    private String role="ROLE_USER";
}
