package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CustomUserDetailsServiceTest {

    @Test
    void loadUserByUsernameReturnsUserDetails() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        User user = new User();
        user.setUsername("sam");
        user.setPassword("p");
        when(repo.findByUsername("sam")).thenReturn(Optional.of(user));

        CustomUserDetailsService svc = new CustomUserDetailsService(repo);
        UserDetails details = svc.loadUserByUsername("sam");

        assertNotNull(details);
        assertEquals("sam", details.getUsername());
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.findByUsername("nope")).thenReturn(Optional.empty());

        CustomUserDetailsService svc = new CustomUserDetailsService(repo);
        assertThrows(UsernameNotFoundException.class, () -> svc.loadUserByUsername("nope"));
    }
}
