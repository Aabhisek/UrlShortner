# UrlShortner — README

This document describes the current application flow, authentication & authorization, configuration, how to run and test the project, and useful notes for development.

## Overview
UrlShortner is a Spring Boot REST service that provides URL shortening and JWT-based authentication. Key components:
- User entity and JPA repository (users stored in a relational DB)
- JWT service to issue and validate access tokens
- Authentication endpoints: register and login
- Url creation endpoint, protected by JWT authentication

## Authentication & Authorization Flow
1. Register (POST /auth/register)
   - Accepts a JSON RegisterRequest (username, email, password)
   - Validates uniqueness, encodes password with BCrypt, saves User with role ROLE_USER
2. Login (POST /auth/login)
   - Accepts LoginRequest (username, password)
   - Uses AuthenticationManager to authenticate credentials
   - On success returns AuthResponse containing a JWT access token
3. Using the token
   - Client includes header: Authorization: Bearer <token>
   - JwtAuthenticationFilter reads and validates token, loads user via CustomUserDetailsService, and sets SecurityContext
   - Protected endpoints (e.g., POST /urls) require a valid JWT

## Important endpoints
- POST /auth/register — register a new user
- POST /auth/login — get JWT token (AuthResponse.token)
- POST /urls — create a short URL (requires Authorization header)

Request/response DTOs are simple records (LoginRequest, RegisterRequest, AuthResponse, CreateUrlRequest, UrlResponse).

## Configuration (application.properties)
Set the following in src/main/resources/application.properties or via environment variables:

- spring.datasource.url, spring.datasource.username, spring.datasource.password — DB connection
- jwt.secret — a sufficiently long random secret (HMAC key). Example in project: a 64-hex string
- jwt.expiration — token TTL in milliseconds (e.g., 3600000 for 1 hour)

Example:
```
spring.datasource.url=jdbc:mysql://localhost:3306/urlshortenerdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
jwt.secret=5250db4b... (64 hex chars)
jwt.expiration=3600000
```

Security notes: keep jwt.secret out of source control and use environment variables or a secrets manager in production.

## How to run
Prerequisites: Java 21+, Maven, a running MySQL instance (or change datasource to H2 for local testing).

Build and run:
- mvn clean package
- java -jar target/UrlShortner-0.0.1-SNAPSHOT.jar

Or run from IDE as a Spring Boot application (com.abhisek.urlshortner.UrlShortnerApplication).

## Testing
Unit tests are present under src/test/java and executed with Maven. Coverage is measured via JaCoCo.

Run tests:
- mvn test
Generate coverage report (HTML + XML):
- mvn test jacoco:report
Report location: target/site/jacoco/index.html

The project currently contains unit tests for JwtService, CustomUserDetails, UrlService, JwtAuthenticationFilter, CustomUserDetailsService, AuthController and UrlController. Aim is to increase coverage to ~90%.

## Development notes & next tasks
- Refresh tokens: currently only access tokens are issued. Add refresh-token flow (store refresh tokens server-side or use rotating refresh tokens) if you need long-lived sessions.
- Email verification and password-reset flows are not implemented — add if required.
- Improve error handling and return appropriate HTTP status codes and JSON error bodies.
- Production hardening: HTTPS, CORS, rate limiting, account lockout, monitoring/logging, DB migrations (Flyway/Liquibase).
- Consider moving JWT settings to a @ConfigurationProperties class for typed binding.

## Useful commands
- mvn test — run tests
- mvn test jacoco:report — run tests and create coverage report
- mvn -DskipTests package — build without running tests

If you want, update this README to include curl examples for each endpoint and Docker instructions — tell me which one to add.
