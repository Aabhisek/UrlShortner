package com.abhisek.urlshortner.Service;

import com.abhisek.urlshortner.Entiry.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    @Test
    void exposesUserProperties() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("secret");
        user.setRole("ROLE_ADMIN");

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("alice", details.getUsername());
        assertEquals("secret", details.getPassword());

        Collection<? extends GrantedAuthority> auth = details.getAuthorities();
        assertNotNull(auth);
        assertTrue(auth.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}
