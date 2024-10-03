# MovieBookingApi

## Overview

This project implements JWT authentication and generates individual access and refresh tokens for users. It allows users to access certain files based on their permissions.

## Features

- **JWT Authentication:** Secure user authentication using JSON Web Tokens.
- **Access and Refresh Tokens:** Individual tokens are generated for each user.
- **User Permissions:** 
  - **Users:** Can view, view all, paginate results, and sort by movie title, release year, or cast.
  - **Admins:** Have permissions to add new movies.
- **CRUD Operations:** Perform create, read, update, and delete operations through designated API endpoints.
- **File Handling:** Manage files associated with movie data.
- **Centralized Exception Handling:** Handle exceptions globally across the application.
- **Custom Exceptions:** Tailor exception messages for specific error cases.
- **Forgot Password Feature:** Allow users to reset their passwords securely.
- **SMTP Integration:** Send emails for password resets and notifications using Spring Boot.

## API Endpoints

### User Endpoints

- **Register User**
  - `POST /api/auth/register`
  - Request Body: `{ "username": "string", "password": "string", "email": "string" }`
  
- **Login User**
  - `POST /api/auth/login`
  - Request Body: `{ "username": "string", "password": "string" }`
  - Response: `{ "accessToken": "string", "refreshToken": "string" }`

### Movie Endpoints

- **Get All Movies**
  - `GET /api/movies`
  - Description: Fetch a list of all movies. Pagination and sorting can be applied.

- **Get Movie by ID**
  - `GET /api/movies/{id}`
  - Description: Fetch details of a specific movie.

- **Add Movie (Admin Only)**
  - `POST /api/movies`
  - Request Body: `{ "title": "string", "releaseYear": "int", "cast": ["string"] }`


