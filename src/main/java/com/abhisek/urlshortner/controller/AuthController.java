package com.abhisek.urlshortner.controller;

import com.abhisek.urlshortner.Entiry.User;
import com.abhisek.urlshortner.Service.JwtService;
import com.abhisek.urlshortner.dto.AuthResponse;
import com.abhisek.urlshortner.dto.LoginRequest;
import com.abhisek.urlshortner.dto.RegisterRequest;
import com.abhisek.urlshortner.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            return "Username already exists";
        }

        if (userRepository.existsByEmail(request.email())) {
            return "Email already exists";
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        return "Registration successful";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.username(), request.password())
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String token =
                jwtService.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
