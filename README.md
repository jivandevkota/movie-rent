# Movie Rent

A full-stack movie rental application built with **Spring Boot** (backend) and **Angular** (frontend).

## Architecture

```
movie-rent/
├── movie_rental_backend/   # Spring Boot REST API
└── movie_rent_frontend/    # Angular client
```

## Backend

- **Framework:** Spring Boot 4.0.6 (Java 17)
- **Database:** MySQL with JPA/Hibernate
- **Key dependencies:** Spring Web MVC, Spring Data JPA, Lombok, Validation
- **Domain model:** Film, Category, Actor, Customer, Rental, Payment, Inventory, Staff, Store, Address, City, Country, Language
- **REST endpoints:** Film and Category CRUD operations

### Run

```bash
cd movie_rental_backend
./mvnw spring-boot:run
```

## Frontend

- **Framework:** Angular 16.2 with Angular Material
- **Features:** Film browsing, film details view, home page
- **Routing:** Client-side routing with Angular Router

### Run

```bash
cd movie_rent_frontend
npm install
ng serve
```

Navigate to `http://localhost:4200`.

## API Documentation

| Method | Endpoint              | Description          |
|--------|-----------------------|----------------------|
| GET    | `/api/films`          | List all films       |
| GET    | `/api/films/{id}`     | Get film by ID       |
| GET    | `/api/categories`     | List all categories  |

## Tech Stack

**Backend:** Java 17, Spring Boot, Spring Data JPA, MySQL, Lombok, Maven

**Frontend:** Angular 16, TypeScript, Angular Material, RxJS

## Database

Configure your MySQL connection in `movie_rental_backend/src/main/resources/application.yml`.
