# Timesheet App - Setup Guide

## Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8+

## Backend (Spring Boot)

1. Create MySQL database (optional - app creates it if configured):
   ```sql
   CREATE DATABASE timesheets_db;
   ```

2. Configure database in `backend/src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/timesheets_db?...
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Run backend:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
   Backend runs at http://localhost:8080

## Frontend (React)

1. Copy env example and configure API URL (optional, defaults to http://localhost:8080):
   ```bash
   cd web
   cp .env.example .env
   # Edit .env: REACT_APP_API_URL=http://localhost:8080
   ```

2. Run frontend:
   ```bash
   npm install
   npm start
   ```
   Frontend runs at http://localhost:3000

## API Endpoints (per docs)
- POST /api/auth/register - Register new user
- POST /api/auth/login - Login (returns JWT)
- POST /api/auth/logout - Logout (invalidates session)
- GET /api/user/me - Get current user (protected, requires Bearer token)

Passwords are hashed with BCrypt. JWT tokens are stored in localStorage.
