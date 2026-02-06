# Timesheet API

## Base URL
`http://localhost:8080` (or set `REACT_APP_API_URL` in frontend `.env`)

## Auth Endpoints

### POST /api/auth/register
Register a new user.

**Request:**
```json
{
  "username": "string",
  "email": "string",
  "password": "string"
}
```

**Response (201):**
```json
{
  "token": "jwt-token",
  "user": {
    "userId": 1,
    "username": "string",
    "email": "string"
  }
}
```

### POST /api/auth/login
Authenticate and get token. `username` can be username or email.

**Request:**
```json
{
  "username": "string",
  "password": "string"
}
```

**Response (200):**
```json
{
  "token": "jwt-token",
  "user": {
    "userId": 1,
    "username": "string",
    "email": "string"
  }
}
```

### POST /api/auth/logout
Invalidate current session. Requires `Authorization: Bearer <token>`.

**Response (200):** Empty

### GET /api/user/me (Protected)
Get current user profile. Requires `Authorization: Bearer <token>`.

**Response (200):**
```json
{
  "userId": 1,
  "username": "string",
  "email": "string"
}
```

## Database
MySQL with tables: `users`, `user_sessions`. Passwords are hashed with BCrypt.
