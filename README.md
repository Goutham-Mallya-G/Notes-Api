# Notes API

A RESTful Notes Management API built with Spring Boot. The project
focuses on learning backend development concepts such as authentication,
authorization, data modeling, scheduling, pagination, validation, and
clean layered architecture.

------------------------------------------------------------------------

## Features

### Authentication

-   User Registration
-   User Login
-   JWT Authentication
-   BCrypt Password Encryption
-   Protected Endpoints with Spring Security

### Categories

-   Create Category
-   Update Category
-   Delete Category
-   List User Categories

### Notes

-   Create Note
-   Get Note by ID
-   List Notes
-   Update Note
-   Move Note Between Categories

### Note Management

-   Archive / Unarchive
-   Favorite / Unfavorite
-   Move to Trash
-   Restore from Trash
-   Permanent Delete

### Search

-   Search by Title
-   Search by Content
-   Combined Search

### Pagination

-   Paginated Note Listing
-   Paginated Search Results

### Background Jobs

-   Automatic cleanup of trashed notes after 30 days using Spring
    Scheduler

### Validation & Error Handling

-   Bean Validation
-   Global Exception Handling
-   Custom Exceptions

------------------------------------------------------------------------

## Tech Stack

-   Java 21
-   Spring Boot
-   Spring Security
-   Spring Data JPA
-   Hibernate
-   MySQL
-   JWT
-   Maven

------------------------------------------------------------------------

## Project Structure

``` text
src
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── scheduler
├── security
├── service
└── util
```

------------------------------------------------------------------------

## Architecture

    Client
       │
       ▼
    Spring Security
       │
    JWT Filter
       │
    Controller
       │
    Service
       │
    Repository
       │
    MySQL

------------------------------------------------------------------------

## Entity Relationships

    User
     └── Categories (1:N)
            └── Notes (1:N)

------------------------------------------------------------------------

## Main API Endpoints

### Authentication

-   POST /users/register
-   POST /users/login

### Categories

-   GET /categories
-   POST /categories
-   PUT /categories/{id}
-   DELETE /categories/{id}

### Notes

-   GET /notes
-   GET /notes/{id}
-   POST /notes
-   PUT /notes/{id}
-   DELETE /notes/{id}

### Archive

-   PATCH /notes/archive/{id}
-   PATCH /notes/unArchive/{id}
-   GET /notes/archived

### Favorites

-   PATCH /notes/favorite/{id}
-   PATCH /notes/unFavorite/{id}
-   GET /notes/favorites

### Trash

-   PATCH /notes/trash/{id}
-   PATCH /notes/restore/{id}
-   GET /notes/trash

### Search

-   GET /notes/search

------------------------------------------------------------------------

## Getting Started

### Clone the repository

``` bash
git clone https://github.com/Goutham-Mallya-G/Notes-Api
cd Notes-API
```

### Configure the database

Update `application.properties`:

``` properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

jwt.secret=
jwt.expiration=
```

### Run the application

``` bash
mvn spring-boot:run
```

------------------------------------------------------------------------

## Current Status

Completed:

-   JWT Authentication
-   Categories
-   Notes CRUD
-   Search
-   Pagination
-   Favorites
-   Archive
-   Trash
-   Scheduler
-   Validation
-   Exception Handling

Planned:

-   Swagger / OpenAPI
-   Unit Testing
-   Integration Testing
-   Docker
-   Deployment

------------------------------------------------------------------------

## Learning Objectives

This project was built to gain practical experience with:

-   Spring Boot
-   Spring Security
-   REST API Design
-   JWT Authentication
-   Spring Data JPA
-   Entity Relationships
-   Pagination
-   Scheduling
-   Validation
-   Clean Architecture

------------------------------------------------------------------------

## License

This project is intended for learning purposes.
