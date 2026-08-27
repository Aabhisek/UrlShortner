# UrlShortner

UrlShortner is a compact Spring Boot REST service that provides URL shortening with JWT-based authentication and click tracking.

## Overview & features

- User registration and login (BCrypt password hashing)
- JWT access tokens (HMAC) for stateless authentication
- Create and list short URLs (per-user)
- Redirect short codes to original URLs (302 Found) and record click events
- Persistence with Spring Data JPA (entities: User, UrlMapping, ClickEvent)

## Tech stack

- Java 21
- Spring Boot 4.1.x (Web, Security, Data JPA)
- Spring Security (stateless JWT)
- JJWT (io.jsonwebtoken)
- MySQL (jdbc runtime dependency)
- Maven, Lombok

## Architecture

- REST controllers: AuthController, UrlController, RedirectController
- Services: JwtService (token issue/validation), UrlService (short-code generation & storage)
- Security: JwtAuthenticationFilter validates tokens and populates SecurityContext
- Data model: User owns UrlMapping; ClickEvent records each redirect

## Project structure (top-level)

- src/main/java/com/abhisek/urlshortner
  - controller/  (API controllers)
  - Service/     (business logic & security helpers)
  - config/      (security configuration, JWT filter)
  - Entiry/      (JPA entities)
  - repository/  (Spring Data repositories)
  - dto/         (request/response records)

## APIs (implemented)

- POST /auth/register
  - Body: {"username":"alice","email":"a@a.com","password":"secret"}
  - Response: plain text message

- POST /auth/login
  - Body: {"username":"alice","password":"secret"}
  - Response: {"token":"<JWT>"}

- POST /urls (requires Authorization header)
  - Header: Authorization: Bearer <JWT>
  - Body: {"originalUrl":"https://example.com"}
  - Response: UrlResponse (id, originalUrl, shortUrl, clickCount, createdDate)

- GET /urls (requires Authorization header)
  - Returns: list of UrlResponse objects for the authenticated user

- GET /{shortCode}
  - Public: redirects (302 Found) to originalUrl and records a ClickEvent

## JWT / Security

- Configured via `jwt.secret` and `jwt.expiration` properties (milliseconds)
- SecurityConfig sets SessionCreationPolicy.STATELESS and registers JwtAuthenticationFilter
- Protected endpoints require ROLE_USER; /auth/** is public

## Database

- Uses Spring Data JPA with entities mapped to tables:
  - users, url_mapping, click_event
- Configure DB via `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`

## Setup & Docker (quick)

1. Set environment variables (recommended, do NOT commit):
   - JWT_secret or configure `jwt.secret` in application.properties
   - JWT_expiration (milliseconds)
   - DB_URL, DB_USERNAME, DB_PASSWORD

2. Build and run locally:
   - mvn clean package
   - java -jar target/UrlShortner-0.0.1-SNAPSHOT.jar

Optional local MySQL (example):

  docker run -d --name mysql-local -e MYSQL_ROOT_PASSWORD=pass -e MYSQL_DATABASE=urlshortenerdb -p 3306:3306 mysql:8

## Usage examples

- Register:
  curl -X POST -H "Content-Type: application/json" -d '{"username":"alice","email":"a@a.com","password":"p"}' http://localhost:8080/auth/register

- Login:
  curl -X POST -H "Content-Type: application/json" -d '{"username":"alice","password":"p"}' http://localhost:8080/auth/login

- Create short URL:
  curl -X POST -H "Authorization: Bearer <token>" -H "Content-Type: application/json" -d '{"originalUrl":"https://example.com"}' http://localhost:8080/urls

- Redirect: open http://localhost:8080/{shortCode}

## Future improvements

- Move secrets to a secrets manager (Vault/AWS Secrets Manager)
- Support refresh tokens and revocation
- Input validation, standardized error responses (Problem/JSON)
- Rate limiting and abuse protection for public endpoints
- Database migrations (Flyway/Liquibase) and stronger tests

---

If you want a Dockerfile and docker-compose for the app + MySQL, I can add them next.